package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateWorkoutRequest {

    private String name;
    private String description;
    private List<WorkoutDayRequest> workoutDays;
}