package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutDayRequest {

    private String name;
    private Integer dayOfWeek;
    private Integer dayOrder;
    private List<ExerciseRequest> exercises;
}