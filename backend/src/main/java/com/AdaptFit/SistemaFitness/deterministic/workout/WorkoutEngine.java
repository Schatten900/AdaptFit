package com.AdaptFit.SistemaFitness.deterministic.workout;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLogRepository;
import com.AdaptFit.SistemaFitness.enums.AdjustmentReason;
import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import com.AdaptFit.SistemaFitness.enums.WorkoutSplit;
import com.AdaptFit.SistemaFitness.feedback.Feedback;
import com.AdaptFit.SistemaFitness.feedback.FeedbackRepository;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDay;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDayRepository;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercise;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercisesRepository;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSession;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSessionExercise;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSessionExerciseRepository;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class WorkoutEngine {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final EvolutionLogRepository evolutionLogRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutSessionExerciseRepository workoutSessionExerciseRepository;
    private final FeedbackRepository feedbackRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutExercisesRepository workoutExercisesRepository;

    @Value("${workout.progression.load-increase:0.025}")
    private double loadIncreasePercentage;

    @Value("${workout.progression.load-decrease:0.05}")
    private double loadDecreasePercentage;

    @Value("${workout.progression.stagnation-weeks:3}")
    private int stagnationWeeksThreshold;

    @Value("${workout.deload.volume-reduction:0.35}")
    private double deloadVolumeReduction;

    @Value("${workout.reps.min:8}")
    private double recommendedRepsMin;

    @Value("${workout.reps.max:12}")
    private double recommendedRepsMax;

    public WorkoutEngine(WorkoutPlanRepository workoutPlanRepository,
                        EvolutionLogRepository evolutionLogRepository,
                        WorkoutSessionRepository workoutSessionRepository,
                        WorkoutSessionExerciseRepository workoutSessionExerciseRepository,
                        FeedbackRepository feedbackRepository,
                        WorkoutDayRepository workoutDayRepository,
                        WorkoutExercisesRepository workoutExercisesRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.evolutionLogRepository = evolutionLogRepository;
        this.workoutSessionRepository = workoutSessionRepository;
        this.workoutSessionExerciseRepository = workoutSessionExerciseRepository;
        this.feedbackRepository = feedbackRepository;
        this.workoutDayRepository = workoutDayRepository;
        this.workoutExercisesRepository = workoutExercisesRepository;
    }

    public WorkoutPlan calculateAndSavePlan(ExperienceLevel experienceLevel, int availableDays, Long userId) {
        WorkoutSplit split = WorkoutSplit.getForExperienceLevel(experienceLevel, availableDays);

        WorkoutPlan plan = workoutPlanRepository.findByUserIdAndIsActiveTrue(userId)
                .orElse(new WorkoutPlan());

        plan.setUserId(userId);
        plan.setWorkoutSplit(split);
        plan.setExperienceLevel(experienceLevel);
        plan.setDaysPerWeek(split.getDaysPerWeek());
        plan.setIsActive(true);

        WorkoutPlan savedPlan = workoutPlanRepository.save(plan);

        log.info("Workout plan created/updated for user {}: split={}, daysPerWeek={}", 
                userId, split, split.getDaysPerWeek());

        return savedPlan;
    }

    public Map<Long, Double> calculateNextSessionWeights(Long userId) {
        List<WorkoutSession> recentSessions = workoutSessionRepository.findByUserIdOrderBySessionDateDesc(userId);
        
        if (recentSessions.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, Double> nextWeights = new HashMap<>();
        
        Set<Long> exerciseIds = new HashSet<>();
        for (WorkoutSession session : recentSessions) {
            List<WorkoutSessionExercise> exercises = workoutSessionExerciseRepository.findBySessionId(session.getId());
            for (WorkoutSessionExercise ex : exercises) {
                exerciseIds.add(ex.getExerciseId());
            }
            if (exerciseIds.size() >= 20) {
                break;
            }
        }

        for (Long exerciseId : exerciseIds) {
            List<WorkoutSessionExercise> exerciseHistory = getExerciseHistory(userId, exerciseId, recentSessions);
            
            if (exerciseHistory.isEmpty()) {
                continue;
            }

            double avgWeight = exerciseHistory.stream()
                    .mapToDouble(WorkoutSessionExercise::getWeight)
                    .average()
                    .orElse(0.0);

            double avgReps = exerciseHistory.stream()
                    .mapToInt(WorkoutSessionExercise::getReps)
                    .average()
                    .orElse(0.0);

            double recommendedRepsMin = getRecommendedRepsMin(exerciseId);
            double recommendedRepsMax = getRecommendedRepsMax(exerciseId);

            double newWeight;
            if (avgReps >= recommendedRepsMax) {
                newWeight = avgWeight * (1 + loadIncreasePercentage);
                log.info("User {} exercise {}: progress detected, increasing weight {} -> {}", 
                        userId, exerciseId, avgWeight, newWeight);
            } else if (avgReps < recommendedRepsMin) {
                newWeight = avgWeight * (1 - loadDecreasePercentage);
                log.info("User {} exercise {}: regression detected, decreasing weight {} -> {}", 
                        userId, exerciseId, avgWeight, newWeight);
            } else {
                newWeight = avgWeight;
            }

            nextWeights.put(exerciseId, Math.round(newWeight * 10.0) / 10.0);
        }

        return nextWeights;
    }

    private List<WorkoutSessionExercise> getExerciseHistory(Long userId, Long exerciseId, List<WorkoutSession> recentSessions) {
        List<WorkoutSessionExercise> history = new ArrayList<>();
        
        for (WorkoutSession session : recentSessions) {
            List<WorkoutSessionExercise> exercises = workoutSessionExerciseRepository
                    .findBySessionId(session.getId());
            
            for (WorkoutSessionExercise ex : exercises) {
                if (ex.getExerciseId().equals(exerciseId)) {
                    history.add(ex);
                }
            }
            
            if (history.size() >= 10) {
                break;
            }
        }
        
        return history;
    }

    public boolean checkStagnation(Long userId, Long exerciseId) {
        LocalDate threeWeeksAgo = LocalDate.now().minusWeeks(stagnationWeeksThreshold);
        
        List<WorkoutSession> recentSessions = workoutSessionRepository.findByUserIdAndDateRange(
                userId, 
                java.sql.Timestamp.valueOf(threeWeeksAgo.atStartOfDay()),
                java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay())
        );

        List<WorkoutSessionExercise> exerciseHistory = new ArrayList<>();
        
        for (WorkoutSession session : recentSessions) {
            List<WorkoutSessionExercise> exercises = workoutSessionExerciseRepository
                    .findBySessionId(session.getId());
            
            for (WorkoutSessionExercise ex : exercises) {
                if (ex.getExerciseId().equals(exerciseId)) {
                    exerciseHistory.add(ex);
                }
            }
        }

        if (exerciseHistory.size() < 3) {
            return false;
        }

        double firstWeight = exerciseHistory.get(exerciseHistory.size() - 1).getWeight();
        double lastWeight = exerciseHistory.get(0).getWeight();
        
        double percentChange = ((lastWeight - firstWeight) / firstWeight) * 100;
        
        return Math.abs(percentChange) < 2.0;
    }

    public boolean checkPerformanceDecline(Long userId) {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        
        List<WorkoutSession> sessions = workoutSessionRepository.findByUserIdAndDateRange(
                userId,
                java.sql.Timestamp.valueOf(twoWeeksAgo.atStartOfDay()),
                java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay())
        );

        if (sessions.size() < 2) {
            return false;
        }

        double avgVolumeNow = sessions.get(0).getTotalVolume() != null ? sessions.get(0).getTotalVolume() : 0.0;
        double avgVolumeBefore = sessions.get(sessions.size() - 1).getTotalVolume() != null 
                ? sessions.get(sessions.size() - 1).getTotalVolume() 
                : 0.0;

        if (avgVolumeBefore == 0) {
            return false;
        }

        double declinePercent = ((avgVolumeBefore - avgVolumeNow) / avgVolumeBefore) * 100;
        
        return declinePercent > 10.0;
    }

    public boolean checkHighFatigue(Long userId) {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        
        List<Feedback> recentFeedbacks = feedbackRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .filter(f -> f.getCreatedAt() != null && f.getCreatedAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().isAfter(oneWeekAgo))
                .limit(3)
                .toList();

        if (recentFeedbacks.isEmpty()) {
            return false;
        }

        double avgFatigue = recentFeedbacks.stream()
                .mapToInt(Feedback::getFatigueLevel)
                .average()
                .orElse(0.0);

        return avgFatigue >= 7.0;
    }

    @Transactional
    public WorkoutPlan applyDeload(Long userId) {
        WorkoutPlan currentPlan = workoutPlanRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException("Nenhum plano de treino ativo encontrado para o usuário"));

        currentPlan.setVolumeMultiplier(deloadVolumeReduction);
        workoutPlanRepository.save(currentPlan);

        List<WorkoutDay> userWorkoutDays = workoutDayRepository.findByUserId(userId);
        int exercisesReduced = 0;

        for (WorkoutDay workoutDay : userWorkoutDays) {
            List<WorkoutExercise> exercises = workoutExercisesRepository.findByWorkoutDayId(workoutDay.getId());
            for (WorkoutExercise exercise : exercises) {
                Integer currentSets = exercise.getSets();
                if (currentSets != null && currentSets > 1) {
                    int newSets = Math.max(1, (int) Math.round(currentSets * (1 - deloadVolumeReduction)));
                    exercise.setSets(newSets);
                    workoutExercisesRepository.save(exercise);
                    exercisesReduced++;
                }
            }
        }

        EvolutionLog evolutionLog = new EvolutionLog();
        evolutionLog.setUserId(userId);
        evolutionLog.setAdjustmentReason(AdjustmentReason.OVERTRAINING);
        evolutionLog.setParameterName("deload");
        evolutionLog.setDescription("Deload applied due to performance decline or high fatigue. Volume reduced by " + (deloadVolumeReduction * 100) + "%. Exercises affected: " + exercisesReduced);
        evolutionLogRepository.save(evolutionLog);

        log.info("Deload applied for user {}. Volume reduced by {}%, exercises affected: {}", userId, deloadVolumeReduction * 100, exercisesReduced);

        return currentPlan;
    }

    @Transactional
    public WorkoutPlan changeStimulus(Long userId, Long exerciseId) {
        WorkoutExercise currentExercise = workoutExercisesRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Exercício não encontrado: " + exerciseId));

        Long currentExerciseCatalogId = currentExercise.getExerciseId();

        List<WorkoutExercise> allUserExercises = workoutExercisesRepository.findByWorkoutDayId(currentExercise.getWorkoutDayId());
        
        Optional<Long> alternativeExerciseId = allUserExercises.stream()
                .filter(e -> !e.getExerciseId().equals(currentExerciseCatalogId))
                .map(WorkoutExercise::getExerciseId)
                .findFirst();

        Long newExerciseId;
        String description;

        if (alternativeExerciseId.isPresent()) {
            newExerciseId = alternativeExerciseId.get();
            description = "Exercise changed from " + currentExerciseCatalogId + " to " + newExerciseId;
        } else {
            newExerciseId = currentExerciseCatalogId;
            description = "No alternative exercise found for exercise " + currentExerciseCatalogId + ". Keeping same exercise but logged for review.";
        }

        currentExercise.setExerciseId(newExerciseId);
        workoutExercisesRepository.save(currentExercise);

        EvolutionLog evolutionLog = new EvolutionLog();
        evolutionLog.setUserId(userId);
        evolutionLog.setAdjustmentReason(AdjustmentReason.PROGRESS_STAGNATION);
        evolutionLog.setPreviousValue(currentExerciseCatalogId.doubleValue());
        evolutionLog.setNewValue(newExerciseId.doubleValue());
        evolutionLog.setParameterName("exercise_stimulus_" + exerciseId);
        evolutionLog.setDescription(description);
        evolutionLogRepository.save(evolutionLog);

        log.info("Stimulus changed for user {} exercise {}: {}", userId, exerciseId, description);

        return workoutPlanRepository.findByUserIdAndIsActiveTrue(userId).orElse(null);
    }

    public WorkoutPlan getActivePlan(Long userId) {
        return workoutPlanRepository.findByUserIdAndIsActiveTrue(userId).orElse(null);
    }

    public WorkoutPlan getLatestPlan(Long userId) {
        return workoutPlanRepository.findTopByUserIdOrderByCreatedAtDesc(userId).orElse(null);
    }

    private double getRecommendedRepsMin(Long exerciseId) {
        return recommendedRepsMin;
    }

    private double getRecommendedRepsMax(Long exerciseId) {
        return recommendedRepsMax;
    }
}
