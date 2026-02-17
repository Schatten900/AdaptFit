package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateWorkoutDayRequest {

    private String name;
    private String description;
    private List<WorkoutExerciseRequest> exercises;
}
