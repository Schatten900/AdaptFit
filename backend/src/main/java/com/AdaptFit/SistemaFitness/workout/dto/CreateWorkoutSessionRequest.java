package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateWorkoutSessionRequest {

    private Long workoutId;
    private LocalDateTime sessionDate;
    private Integer durationMinutes;
    private String notes;
}