package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WorkoutSessionResponse {

    private Long id;
    private Long workoutId;
    private String workoutName;
    private Date sessionDate;
    private Integer durationMinutes;
    private String notes;
}
