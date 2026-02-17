package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutDayRequest {

    private String name;
    private String description;
    private Integer dayOfWeek;
    private Integer dayOrder;
    private List<WorkoutExerciseRequest> exercises;
}
