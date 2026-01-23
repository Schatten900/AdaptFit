package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

@Data
public class ExerciseRequest {

    private String name;
    private String description;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private Integer restTimeSeconds;
    private Integer exerciseOrder;
}