package com.AdaptFit.SistemaFitness.feedback.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFeedbackRequest {

    @NotNull(message = "Workout session ID is required")
    private Long workoutSessionId;

    @NotNull(message = "Fatigue level is required")
    @Min(value = 1, message = "Fatigue level must be between 1 and 10")
    @Max(value = 10, message = "Fatigue level must be between 1 and 10")
    private Integer fatigueLevel;

    @NotNull(message = "Muscle soreness is required")
    @Min(value = 1, message = "Muscle soreness must be between 1 and 10")
    @Max(value = 10, message = "Muscle soreness must be between 1 and 10")
    private Integer muscleSoreness;

    private String notes;
}
