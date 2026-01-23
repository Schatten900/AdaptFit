package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkoutSessionResponse {

    private Long id;
    private Long workoutId;
    private String workoutName;
    private LocalDateTime sessionDate;
    private Integer durationMinutes;
    private String notes;
}