package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkoutExerciseRequest {

    private Long exerciseId;

    private Integer sets;

    private Integer reps;

    private BigDecimal weight;

    private Integer restTimeSeconds;

    private Integer exerciseOrder;
}
