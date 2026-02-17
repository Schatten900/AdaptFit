package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WorkoutDayResponse {

    private Long id;
    private String name;
    private String description;
    private Integer dayOfWeek;
    private Integer dayOrder;
    private List<WorkoutExerciseResponse> exercises;
    private Date createdAt;
    private Date updatedAt;
}
