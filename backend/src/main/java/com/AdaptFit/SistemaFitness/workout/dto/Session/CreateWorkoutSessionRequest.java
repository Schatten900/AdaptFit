package com.AdaptFit.SistemaFitness.workout.dto.Session;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateWorkoutSessionRequest {

    private Long workoutDayId;
    private LocalDateTime sessionDate;
    private Integer durationMinutes;
    private String notes;
    private Integer totalReps;
    private Double totalWeight;
    private Double totalVolume;
    private List<ExercisePerformedRequest> exercises;
}