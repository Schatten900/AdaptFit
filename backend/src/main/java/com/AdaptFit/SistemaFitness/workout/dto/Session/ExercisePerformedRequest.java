package com.AdaptFit.SistemaFitness.workout.dto.Session;

import lombok.Data;

import java.util.List;

@Data
public class ExercisePerformedRequest {
    private Long exerciseId;
    private List<SetPerformedRequest> sets;
}
