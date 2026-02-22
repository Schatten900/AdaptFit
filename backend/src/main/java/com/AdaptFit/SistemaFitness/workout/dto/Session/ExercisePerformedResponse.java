package com.AdaptFit.SistemaFitness.workout.dto.Session;

import lombok.Data;

import java.util.List;

@Data
public class ExercisePerformedResponse {
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;
    private List<SetPerformedResponse> sets;
    private Integer totalSets;
    private Integer totalReps;
    private Double totalWeight;
    private Double totalVolume;
}
