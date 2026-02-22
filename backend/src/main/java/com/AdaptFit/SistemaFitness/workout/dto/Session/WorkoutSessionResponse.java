package com.AdaptFit.SistemaFitness.workout.dto.Session;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WorkoutSessionResponse {

    private Long id;
    private Long workoutDayId;
    private String workoutName;
    private Date sessionDate;
    private Integer durationMinutes;
    private String notes;
    private Integer totalReps;
    private Double totalWeight;
    private Double totalVolume;
    private List<ExercisePerformedResponse> exercises;
}
