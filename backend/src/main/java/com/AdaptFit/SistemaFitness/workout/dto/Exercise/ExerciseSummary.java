package com.AdaptFit.SistemaFitness.workout.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseSummary {
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;
    private Integer sets;
    private Integer reps;
    private Double weight;
}
