package com.AdaptFit.SistemaFitness.workout.session;

import com.AdaptFit.SistemaFitness.common.exception.AuthenticationException;
import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDay;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDayRepository;
import com.AdaptFit.SistemaFitness.workout.dto.Evolution.DashboardResponse;
import com.AdaptFit.SistemaFitness.workout.dto.Evolution.EvolutionDataPoint;
import com.AdaptFit.SistemaFitness.workout.dto.Exercise.ExerciseSummary;
import com.AdaptFit.SistemaFitness.workout.dto.History.HistoricoResponse;
import com.AdaptFit.SistemaFitness.workout.dto.Session.CreateWorkoutSessionRequest;
import com.AdaptFit.SistemaFitness.workout.dto.Session.ExercisePerformedRequest;
import com.AdaptFit.SistemaFitness.workout.dto.Session.ExercisePerformedResponse;
import com.AdaptFit.SistemaFitness.workout.dto.Session.SetPerformedRequest;
import com.AdaptFit.SistemaFitness.workout.dto.Session.SetPerformedResponse;
import com.AdaptFit.SistemaFitness.workout.dto.Session.WorkoutSessionResponse;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercise;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercisesRepository;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutSessionExerciseRepository workoutSessionExerciseRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final UserService userService;
    private final WorkoutExercisesRepository workoutExercisesRepository;
    private final ExerciseCatalogRepository exerciseCatalogRepository;

    @Transactional
    public WorkoutSessionResponse createWorkoutSession(CreateWorkoutSessionRequest request) {
        validateCreateWorkoutSessionRequest(request);
        User user = getCurrentUser();
        
        Long workoutDayId = request.getWorkoutDayId();
        
        WorkoutDay workoutDay = workoutDayRepository.findByIdAndUserId(workoutDayId, user.getId())
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));
        
        LocalDate today = LocalDate.now();

        Optional<WorkoutSession> existingSession = workoutSessionRepository
                .findByUserIdAndWorkoutDayIdAndLocalDate(user.getId(), workoutDayId, today);

        if (existingSession.isPresent()) {
            throw new ValidationException("Treino já realizado hoje!");
        }

        WorkoutSession session = mapToWorkoutSessionEntity(request, user.getId(), workoutDayId, today);

        WorkoutSession saved = workoutSessionRepository.save(session);

        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            saveSessionExercises(request, saved.getId(), user.getId(), workoutDayId, today);
        }

        return mapToResponse(saved);
    }

    public List<WorkoutSessionResponse> getWorkoutSessionsForCurrentUser() {
        User user = getCurrentUser();
        List<WorkoutSession> sessions = workoutSessionRepository.findByUserIdOrderBySessionDateDesc(user.getId());
        return sessions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<WorkoutSessionResponse> getWorkoutSessionsByWorkoutDayId(Long workoutDayId) {
        User user = getCurrentUser();
        workoutDayRepository.findByIdAndUserId(workoutDayId, user.getId())
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));
        List<WorkoutSession> sessions = workoutSessionRepository.findByWorkoutDayIdOrderBySessionDateDesc(workoutDayId);
        return sessions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public WorkoutSessionResponse getWorkoutSessionById(Long id) {
        User user = getCurrentUser();
        WorkoutSession session = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão não encontrada"));

        if (!session.getUserId().equals(user.getId())) {
            throw new AuthenticationException("Sessão não pertence ao usuario");
        }

        return mapToResponse(session);
    }

    public WorkoutSessionResponse getLatestSessionForUser() {
        User user = getCurrentUser();
        Optional<WorkoutSession> session = workoutSessionRepository.findFirstByUserIdOrderBySessionDateDesc(user.getId());

        return session.map(this::mapToResponse).orElse(null);
    }

    public WorkoutSessionResponse getLatestSessionByWorkoutDayId(Long workoutDayId) {
        User user = getCurrentUser();
        Optional<WorkoutSession> session = workoutSessionRepository.findLatestByUserIdAndWorkoutDayId(user.getId(), workoutDayId);

        return session.map(this::mapToResponse).orElse(null);
    }

    public Map<String, Object> getSessionStatsForCurrentUser() {
        User user = getCurrentUser();
        return getSessionStats(user.getId());
    }

    public Map<String, Object> getSessionStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", workoutSessionRepository.countByUserId(userId));
        stats.put("totalDurationMinutes", workoutSessionRepository.sumDurationByUserId(userId));
        stats.put("lastSessionDate", workoutSessionRepository.findLastSessionDateByUserId(userId));
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date weekStart = cal.getTime();
        stats.put("daysTrainedThisWeek", workoutSessionRepository.countSessionsInWeek(userId, weekStart));
        
        return stats;
    }

    @Transactional
    public void deleteWorkoutSession(Long id) {
        User user = getCurrentUser();

        WorkoutSession session = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão não encontrada"));

        if (!session.getUserId().equals(user.getId())) {
            throw new AuthenticationException("Sessão não pertence ao usuario");
        }

        workoutSessionRepository.deleteById(id);
    }

    private void validateCreateWorkoutSessionRequest(CreateWorkoutSessionRequest request) {
        if (request == null || request.getWorkoutDayId() == null || request.getSessionDate() == null) {
            throw new ValidationException("Treino invalido");
        }
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private WorkoutSession mapToWorkoutSessionEntity(CreateWorkoutSessionRequest request, Long userId, Long workoutDayId, LocalDate localDate) {
        WorkoutSession session = new WorkoutSession();
        session.setUserId(userId);
        session.setWorkoutDayId(workoutDayId);
        session.setLocalDate(localDate);
        if (request.getSessionDate() != null) {
            session.setSessionDate(java.sql.Timestamp.valueOf(request.getSessionDate()));
        }
        session.setDurationMinutes(request.getDurationMinutes());
        session.setNotes(request.getNotes());
        session.setTotalReps(request.getTotalReps());
        session.setTotalWeight(request.getTotalWeight());
        session.setTotalVolume(request.getTotalVolume());
        return session;
    }

    private WorkoutSessionResponse mapToResponse(WorkoutSession session) {
        WorkoutSessionResponse response = new WorkoutSessionResponse();
        response.setId(session.getId());
        response.setWorkoutDayId(session.getWorkoutDayId());
        response.setSessionDate(session.getSessionDate());
        response.setDurationMinutes(session.getDurationMinutes());
        response.setNotes(session.getNotes());
        response.setTotalReps(session.getTotalReps());
        response.setTotalWeight(session.getTotalWeight());
        response.setTotalVolume(session.getTotalVolume());

        if (session.getWorkoutDayId() != null) {
            WorkoutDay workoutDay = workoutDayRepository.findById(session.getWorkoutDayId()).orElse(null);

            if (workoutDay != null) {
                response.setWorkoutName(workoutDay.getName());
            }
        }

        List<WorkoutSessionExercise> sessionExercises = workoutSessionExerciseRepository.findBySessionId(session.getId());
        if (!sessionExercises.isEmpty()) {
            response.setExercises(mapToExercisePerformedResponses(sessionExercises));
        }

        return response;
    }

    private void saveSessionExercises(CreateWorkoutSessionRequest request, Long sessionId, Long userId, Long workoutDayId, LocalDate sessionDate) {
        for (ExercisePerformedRequest exerciseReq : request.getExercises()) {
            if (exerciseReq.getSets() == null) continue;

            for (SetPerformedRequest setReq : exerciseReq.getSets()) {
                if (setReq.getDone() == null || !setReq.getDone()) continue;

                WorkoutSessionExercise sessionExercise = new WorkoutSessionExercise();
                sessionExercise.setSessionId(sessionId);
                sessionExercise.setExerciseId(exerciseReq.getExerciseId());
                sessionExercise.setWorkoutDayId(workoutDayId);
                sessionExercise.setUserId(userId);
                sessionExercise.setSessionDate(sessionDate);
                sessionExercise.setSetNumber(setReq.getSetNumber());
                sessionExercise.setReps(setReq.getReps());
                sessionExercise.setWeight(setReq.getWeight());
                
                if (setReq.getReps() != null && setReq.getWeight() != null) {
                    sessionExercise.setVolume(setReq.getReps() * setReq.getWeight());
                }

                workoutSessionExerciseRepository.save(sessionExercise);
            }
        }
    }

    private List<ExercisePerformedResponse> mapToExercisePerformedResponses(List<WorkoutSessionExercise> sessionExercises) {
        Map<Long, List<WorkoutSessionExercise>> exercisesByExerciseId = sessionExercises.stream()
                .collect(Collectors.groupingBy(WorkoutSessionExercise::getExerciseId));

        List<ExercisePerformedResponse> responses = new ArrayList<>();

        for (Map.Entry<Long, List<WorkoutSessionExercise>> entry : exercisesByExerciseId.entrySet()) {
            ExercisePerformedResponse exerciseResponse = new ExercisePerformedResponse();
            exerciseResponse.setExerciseId(entry.getKey());

            ExerciseCatalog catalog = exerciseCatalogRepository.findById(entry.getKey()).orElse(null);
            exerciseResponse.setExerciseName(catalog != null ? catalog.getName() : "Exercício");
            exerciseResponse.setMuscleGroup(catalog != null ? catalog.getMuscleGroup() : "Desconhecido");

            List<WorkoutSessionExercise> sets = entry.getValue();
            List<SetPerformedResponse> setResponses = new ArrayList<>();

            int totalReps = 0;
            double totalWeight = 0;
            double totalVolume = 0;

            for (WorkoutSessionExercise se : sets) {
                SetPerformedResponse setResponse = new SetPerformedResponse();
                setResponse.setSetNumber(se.getSetNumber());
                setResponse.setReps(se.getReps());
                setResponse.setWeight(se.getWeight());
                setResponse.setVolume(se.getVolume());
                setResponses.add(setResponse);

                if (se.getReps() != null) totalReps += se.getReps();
                if (se.getWeight() != null) totalWeight += se.getWeight();
                if (se.getVolume() != null) totalVolume += se.getVolume();
            }

            setResponses.sort(Comparator.comparingInt(SetPerformedResponse::getSetNumber));
            exerciseResponse.setSets(setResponses);
            exerciseResponse.setTotalSets(sets.size());
            exerciseResponse.setTotalReps(totalReps);
            exerciseResponse.setTotalWeight(totalWeight);
            exerciseResponse.setTotalVolume(totalVolume);

            responses.add(exerciseResponse);
        }

        return responses;
    }

    private <T> T handleCircuitBreakerFallback(Throwable t) {
        if (t instanceof RuntimeException re) {
            throw re;
        }
        throw new RuntimeException("Serviço temporariamente indisponível", t);
    }

    public List<HistoricoResponse> getHistory(String period, String muscleGroup, Long exerciseId, Long workoutId) {
        User user = getCurrentUser();
        Date[] dates = getDateRange(period);
        
        List<WorkoutSession> sessions;
        
        if (workoutId != null) {
            sessions = workoutSessionRepository.findByUserIdAndWorkoutDayIdAndDateRange(
                user.getId(), workoutId, dates[0], dates[1]);
        } else {
            sessions = workoutSessionRepository.findByUserIdAndDateRange(user.getId(), dates[0], dates[1]);
        }
        
        return sessions.stream()
            .map(session -> mapToHistoricoResponse(session, muscleGroup, exerciseId))
            .collect(Collectors.toList());
    }

    public DashboardResponse getDashboard(String period, String muscleGroup, Long exerciseId, Long workoutId) {
        User user = getCurrentUser();
        Date[] dates = getDateRange(period);
        
        List<WorkoutSession> sessions;
        if (workoutId != null) {
            sessions = workoutSessionRepository.findByUserIdAndWorkoutDayIdAndDateRange(
                user.getId(), workoutId, dates[0], dates[1]);
        } else {
            sessions = workoutSessionRepository.findByUserIdAndDateRange(user.getId(), dates[0], dates[1]);
        }
        
        DashboardResponse dashboard = new DashboardResponse();
        
        Long totalSessions = workoutSessionRepository.countByUserIdAndDateRange(user.getId(), dates[0], dates[1]);
        Long totalDuration = workoutSessionRepository.sumDurationByUserIdAndDateRange(user.getId(), dates[0], dates[1]);
        
        if (workoutId != null) {
            totalSessions = (long) sessions.size();
            totalDuration = sessions.stream()
                .mapToLong(s -> s.getDurationMinutes() != null ? s.getDurationMinutes() : 0)
                .sum();
        }
        
        dashboard.setTotalSessions(totalSessions.intValue());
        dashboard.setTotalDurationMinutes(totalDuration != null ? totalDuration.intValue() : 0);
        dashboard.setAverageDuration(totalSessions != null && totalSessions > 0 
            ? (double) totalDuration / totalSessions 
            : 0.0);
        
        Map<String, Long> sessionsByMuscleGroup = calculateSessionsByMuscleGroup(user.getId(), dates[0], dates[1], muscleGroup, exerciseId, workoutId);
        dashboard.setSessionsByMuscleGroup(sessionsByMuscleGroup);
        
        List<EvolutionDataPoint> evolutionData = calculateEvolutionData(user.getId(), dates[0], dates[1], period, exerciseId, workoutId);
        dashboard.setEvolutionData(evolutionData);
        
        Map<String, Long> workoutDistribution = calculateWorkoutDistribution(user.getId(), dates[0], dates[1], workoutId);
        dashboard.setWorkoutDistribution(workoutDistribution);
        
        int totalReps = sessions.stream()
            .mapToInt(s -> s.getTotalReps() != null ? s.getTotalReps() : 0)
            .sum();
        double totalWeight = sessions.stream()
            .mapToDouble(s -> s.getTotalWeight() != null ? s.getTotalWeight() : 0)
            .sum();
        double totalVolume = sessions.stream()
            .mapToDouble(s -> s.getTotalVolume() != null ? s.getTotalVolume() : 0)
            .sum();
        
        dashboard.setTotalReps(totalReps);
        dashboard.setTotalWeight(totalWeight);
        dashboard.setTotalVolume(totalVolume);
        
        return dashboard;
    }

    private Date[] getDateRange(String period) {
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        switch (period.toLowerCase()) {
            case "week":
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case "month":
                cal.add(Calendar.MONTH, -1);
                break;
            case "year":
                cal.add(Calendar.YEAR, -1);
                break;
            default:
                cal.add(Calendar.MONTH, -1);
        }
        
        return new Date[]{cal.getTime(), endDate};
    }

    private HistoricoResponse mapToHistoricoResponse(WorkoutSession session, String muscleGroupFilter, Long exerciseIdFilter) {
        HistoricoResponse response = new HistoricoResponse();
        response.setSessionId(session.getId());
        response.setWorkoutDayId(session.getWorkoutDayId());
        response.setSessionDate(session.getSessionDate());
        response.setDurationMinutes(session.getDurationMinutes());
        response.setNotes(session.getNotes());
        
        if (session.getWorkoutDayId() != null) {
            WorkoutDay workoutDay = workoutDayRepository.findById(session.getWorkoutDayId()).orElse(null);
            if (workoutDay != null) {
                response.setWorkoutName(workoutDay.getName());
            }
        }
        
        List<WorkoutExercise> exercises = workoutExercisesRepository.findByWorkoutDayId(session.getWorkoutDayId());
        List<ExerciseSummary> exerciseSummaries = exercises.stream()
            .map(ex -> {
                ExerciseCatalog catalog = exerciseCatalogRepository.findById(ex.getExerciseId()).orElse(null);
                String muscleGroup = catalog != null ? catalog.getMuscleGroup() : "Desconhecido";
                
                if (muscleGroupFilter != null && !muscleGroupFilter.equalsIgnoreCase(muscleGroup)) {
                    return null;
                }
                
                if (exerciseIdFilter != null && !exerciseIdFilter.equals(ex.getExerciseId())) {
                    return null;
                }
                
                ExerciseSummary summary = new ExerciseSummary();
                summary.setExerciseId(ex.getExerciseId());
                summary.setExerciseName(catalog != null ? catalog.getName() : "Exercício");
                summary.setMuscleGroup(muscleGroup);
                summary.setSets(ex.getSets());
                summary.setReps(ex.getReps());
                summary.setWeight(ex.getWeight() != null ? ex.getWeight().doubleValue() : null);
                return summary;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        response.setExercises(exerciseSummaries);
        return response;
    }

    private Map<String, Long> calculateSessionsByMuscleGroup(Long userId, Date startDate, Date endDate, String muscleGroupFilter, Long exerciseIdFilter, Long workoutIdFilter) {
        Map<String, Long> result = new LinkedHashMap<>();
        
        List<WorkoutSession> sessions;
        if (workoutIdFilter != null) {
            sessions = workoutSessionRepository.findByUserIdAndWorkoutDayIdAndDateRange(userId, workoutIdFilter, startDate, endDate);
        } else {
            sessions = workoutSessionRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        }
        
        for (WorkoutSession session : sessions) {
            Set<String> sessionMuscleGroups = new HashSet<>();
            List<WorkoutExercise> exercises = workoutExercisesRepository.findByWorkoutDayId(session.getWorkoutDayId());
            
            for (WorkoutExercise ex : exercises) {
                ExerciseCatalog catalog = exerciseCatalogRepository.findById(ex.getExerciseId()).orElse(null);
                String muscleGroup = catalog != null ? catalog.getMuscleGroup() : "Desconhecido";
                
                if (muscleGroupFilter != null && !muscleGroupFilter.equalsIgnoreCase(muscleGroup)) {
                    continue;
                }
                
                if (exerciseIdFilter != null && !exerciseIdFilter.equals(ex.getExerciseId())) {
                    continue;
                }
                
                if (!sessionMuscleGroups.contains(muscleGroup)) {
                    result.put(muscleGroup, result.getOrDefault(muscleGroup, 0L) + 1);
                    sessionMuscleGroups.add(muscleGroup);
                }
            }
        }
        
        return result;
    }

    private List<EvolutionDataPoint> calculateEvolutionData(Long userId, Date startDate, Date endDate, String period, Long exerciseIdFilter, Long workoutIdFilter) {
        List<EvolutionDataPoint> result = new ArrayList<>();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        int increment;
        String dateFormat;
        
        switch (period.toLowerCase()) {
            case "week":
                increment = Calendar.DAY_OF_WEEK;
                dateFormat = "EEE";
                break;
            case "month":
                increment = Calendar.WEEK_OF_YEAR;
                dateFormat = "dd/MM";
                break;
            case "year":
                increment = Calendar.MONTH;
                dateFormat = "MMM";
                break;
            default:
                increment = Calendar.WEEK_OF_YEAR;
                dateFormat = "dd/MM";
        }
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat);
        
        while (cal.getTime().before(endDate)) {
            Calendar weekEnd = (Calendar) cal.clone();
            weekEnd.add(increment, 1);
            
            if (weekEnd.getTime().after(endDate)) {
                weekEnd.setTime(endDate);
            }
            
            List<WorkoutSession> sessions;
            if (workoutIdFilter != null) {
                sessions = workoutSessionRepository.findByUserIdAndWorkoutDayIdAndDateRange(
                    userId, workoutIdFilter, cal.getTime(), weekEnd.getTime());
            } else {
                sessions = workoutSessionRepository.findByUserIdAndDateRange(userId, cal.getTime(), weekEnd.getTime());
            }
            
            if (exerciseIdFilter != null) {
                final Long exerciseId = exerciseIdFilter;
                sessions = sessions.stream()
                    .filter(s -> {
                        List<WorkoutExercise> exercises = workoutExercisesRepository.findByWorkoutDayId(s.getWorkoutDayId());
                        return exercises.stream().anyMatch(e -> exerciseId.equals(e.getExerciseId()));
                    })
                    .collect(Collectors.toList());
            }
            
            Long count = (long) sessions.size();
            Long duration = sessions.stream()
                .mapToLong(s -> s.getDurationMinutes() != null ? s.getDurationMinutes() : 0)
                .sum();
            int reps = sessions.stream()
                .mapToInt(s -> s.getTotalReps() != null ? s.getTotalReps() : 0)
                .sum();
            double weight = sessions.stream()
                .mapToDouble(s -> s.getTotalWeight() != null ? s.getTotalWeight() : 0)
                .sum();
            double volume = sessions.stream()
                .mapToDouble(s -> s.getTotalVolume() != null ? s.getTotalVolume() : 0)
                .sum();
            
            EvolutionDataPoint point = new EvolutionDataPoint();
            point.setDate(cal.getTime());
            point.setSessionsCount(count != null ? count.intValue() : 0);
            point.setTotalDuration(duration != null ? duration.intValue() : 0);
            point.setTotalReps(reps);
            point.setTotalWeight(weight);
            point.setTotalVolume(volume);
            point.setPeriod(sdf.format(cal.getTime()));
            
            result.add(point);
            
            cal.add(increment, 1);
        }
        
        return result;
    }

    private Map<String, Long> calculateWorkoutDistribution(Long userId, Date startDate, Date endDate, Long workoutIdFilter) {
        Map<String, Long> result = new LinkedHashMap<>();
        
        List<WorkoutSession> sessions;
        if (workoutIdFilter != null) {
            sessions = workoutSessionRepository.findByUserIdAndWorkoutDayIdAndDateRange(
                userId, workoutIdFilter, startDate, endDate);
        } else {
            sessions = workoutSessionRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        }
        
        for (WorkoutSession session : sessions) {
            WorkoutDay workoutDay = workoutDayRepository.findById(session.getWorkoutDayId()).orElse(null);
            String workoutName = workoutDay != null ? workoutDay.getName() : "Treino";
            
            result.put(workoutName, result.getOrDefault(workoutName, 0L) + 1);
        }
        
        return result;
    }

}
