package com.AdaptFit.SistemaFitness.deterministic;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLogRepository;
import com.AdaptFit.SistemaFitness.deterministic.nutricional.MetabolicEngine;
import com.AdaptFit.SistemaFitness.deterministic.nutricional.NutritionalPlan;
import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutEngine;
import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutPlan;
import com.AdaptFit.SistemaFitness.enums.ActivityLevel;
import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.AdaptFit.SistemaFitness.profile.ProfileService;
import com.AdaptFit.SistemaFitness.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeterministicService {

    private final MetabolicEngine metabolicEngine;
    private final WorkoutEngine workoutEngine;
    private final EvolutionLogRepository evolutionLogRepository;
    private final ProfileService profileService;
    private final UserService userService;

    public NutritionalPlan createNutritionalPlan(ActivityLevel activityLevel) {
        Long userId = userService.getCurrentUserId();
        Profile profile = profileService.getCurrentUserProfile();
        
        if (profile == null) {
            throw new ValidationException("Perfil do usuário não encontrado. Complete seu perfil primeiro.");
        }
        
        return metabolicEngine.calculateAndSavePlan(profile, activityLevel);
    }

    public NutritionalPlan getActiveNutritionalPlan() {
        Long userId = userService.getCurrentUserId();
        NutritionalPlan plan = metabolicEngine.getActivePlan(userId);
        if (plan == null) {
            throw new NotFoundException("Nenhum plano nutricional ativo encontrado");
        }
        return plan;
    }

    public NutritionalPlan getLatestNutritionalPlan() {
        Long userId = userService.getCurrentUserId();
        NutritionalPlan plan = metabolicEngine.getLatestPlan(userId);
        if (plan == null) {
            throw new NotFoundException("Nenhum plano nutricional encontrado");
        }
        return plan;
    }

    public WorkoutPlan createWorkoutPlan(ExperienceLevel experienceLevel, int availableDays) {
        Long userId = userService.getCurrentUserId();
        return workoutEngine.calculateAndSavePlan(experienceLevel, availableDays, userId);
    }

    public WorkoutPlan getActiveWorkoutPlan() {
        Long userId = userService.getCurrentUserId();
        WorkoutPlan plan = workoutEngine.getActivePlan(userId);
        if (plan == null) {
            throw new NotFoundException("Nenhum plano de treino ativo encontrado");
        }
        return plan;
    }

    public WorkoutPlan getLatestWorkoutPlan() {
        Long userId = userService.getCurrentUserId();
        WorkoutPlan plan = workoutEngine.getLatestPlan(userId);
        if (plan == null) {
            throw new NotFoundException("Nenhum plano de treino encontrado");
        }
        return plan;
    }

    public void checkAndAdjustPlans() {
        Long userId = userService.getCurrentUserId();
        boolean deloadApplied = false;
        
        if (workoutEngine.checkHighFatigue(userId)) {
            log.info("High fatigue detected for user {}, applying deload", userId);
            workoutEngine.applyDeload(userId);
            deloadApplied = true;
        }
        
        if (!deloadApplied && workoutEngine.checkPerformanceDecline(userId)) {
            log.info("Performance decline detected for user {}, applying deload", userId);
            workoutEngine.applyDeload(userId);
        }
    }

    public List<EvolutionLog> getEvolutionHistory() {
        Long userId = userService.getCurrentUserId();
        return evolutionLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Map<Long, Double> getRecommendedWeights() {
        Long userId = userService.getCurrentUserId();
        return workoutEngine.calculateNextSessionWeights(userId);
    }
}
