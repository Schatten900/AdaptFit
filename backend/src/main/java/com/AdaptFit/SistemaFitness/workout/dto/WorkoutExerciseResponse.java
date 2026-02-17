package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkoutExerciseResponse {

    private Long id;

    private Long exerciseId;
    private String name;
    private String description;
    private String muscleGroup;
    private String equipment;
    private Boolean isBodyweight;

    private Integer sets;
    private Integer reps;
    private BigDecimal weight;
    private Integer restTimeSeconds;
    private Integer exerciseOrder;
}
