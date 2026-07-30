package com.AdaptFit.SistemaFitness.deterministic;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.deterministic.dto.*;
import com.AdaptFit.SistemaFitness.deterministic.dto.nutritional.CreateNutritionalPlanRequest;
import com.AdaptFit.SistemaFitness.deterministic.dto.nutritional.NutritionalPlanResponse;
import com.AdaptFit.SistemaFitness.deterministic.dto.workout.CreateWorkoutPlanRequest;
import com.AdaptFit.SistemaFitness.deterministic.dto.workout.WorkoutPlanResponse;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.deterministic.nutricional.NutritionalPlan;
import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutPlan;
import com.AdaptFit.SistemaFitness.enums.ActivityLevel;
import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/deterministic")
@RequiredArgsConstructor
@Validated
public class DeterministicController {

    private final DeterministicService deterministicService;

    @PostMapping("/nutrition")
    public ResponseEntity<ApiResponse<NutritionalPlanResponse>> createNutritionalPlan(
            @Valid @RequestBody CreateNutritionalPlanRequest request) {
        
        ActivityLevel activityLevel = request.getActivityLevel();
        NutritionalPlan plan = deterministicService.createNutritionalPlan(activityLevel);
        
        return ResponseEntity.ok(ApiResponse.success(NutritionalPlanResponse.fromEntity(plan), "Plano nutricional criado com sucesso"));
    }

    @GetMapping("/nutrition/active")
    public ResponseEntity<ApiResponse<NutritionalPlanResponse>> getActiveNutritionalPlan() {
        NutritionalPlan plan = deterministicService.getActiveNutritionalPlan();
        return ResponseEntity.ok(ApiResponse.success(NutritionalPlanResponse.fromEntity(plan)));
    }

    @GetMapping("/nutrition/latest")
    public ResponseEntity<ApiResponse<NutritionalPlanResponse>> getLatestNutritionalPlan() {
        NutritionalPlan plan = deterministicService.getLatestNutritionalPlan();
        return ResponseEntity.ok(ApiResponse.success(NutritionalPlanResponse.fromEntity(plan)));
    }

    @PostMapping("/workout")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> createWorkoutPlan(
            @Valid @RequestBody CreateWorkoutPlanRequest request) {
        
        ExperienceLevel experienceLevel = request.getExperienceLevel();
        Integer availableDays = request.getAvailableDays();
        
        WorkoutPlan plan = deterministicService.createWorkoutPlan(experienceLevel, availableDays);
        
        return ResponseEntity.ok(ApiResponse.success(WorkoutPlanResponse.fromEntity(plan), "Plano de treino criado com sucesso"));
    }

    @GetMapping("/workout/active")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> getActiveWorkoutPlan() {
        WorkoutPlan plan = deterministicService.getActiveWorkoutPlan();
        return ResponseEntity.ok(ApiResponse.success(WorkoutPlanResponse.fromEntity(plan)));
    }

    @GetMapping("/workout/latest")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> getLatestWorkoutPlan() {
        WorkoutPlan plan = deterministicService.getLatestWorkoutPlan();
        return ResponseEntity.ok(ApiResponse.success(WorkoutPlanResponse.fromEntity(plan)));
    }

    @PostMapping("/check-adjustments")
    public ResponseEntity<ApiResponse<String>> checkAndAdjustPlans() {
        deterministicService.checkAndAdjustPlans();
        
        return ResponseEntity.ok(ApiResponse.success("Verificação concluída"));
    }

    @GetMapping("/evolution")
    public ResponseEntity<ApiResponse<List<EvolutionLogResponse>>> getEvolutionHistory() {
        List<EvolutionLog> logs = deterministicService.getEvolutionHistory();
        
        List<EvolutionLogResponse> responses = logs.stream()
                .map(EvolutionLogResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/workout/recommended-weights")
    public ResponseEntity<ApiResponse<Map<Long, Double>>> getRecommendedWeights() {
        Map<Long, Double> weights = deterministicService.getRecommendedWeights();
        
        return ResponseEntity.ok(ApiResponse.success(weights, "Pesos recomendados para próxima sessão"));
    }
}
