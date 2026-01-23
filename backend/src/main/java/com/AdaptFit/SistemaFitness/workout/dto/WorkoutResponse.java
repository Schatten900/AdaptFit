package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkoutResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<WorkoutDayResponse> workoutDays;
}