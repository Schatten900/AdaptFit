package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutDayResponse {

    private Long id;
    private String name;
    private Integer dayOfWeek;
    private Integer dayOrder;
    private List<ExerciseResponse> exercises;
}