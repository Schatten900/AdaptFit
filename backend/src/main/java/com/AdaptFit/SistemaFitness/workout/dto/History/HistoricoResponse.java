package com.AdaptFit.SistemaFitness.workout.dto.History;

import com.AdaptFit.SistemaFitness.workout.dto.Exercise.ExerciseSummary;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class HistoricoResponse {
    private Long sessionId;
    private Long workoutDayId;
    private String workoutName;
    private Date sessionDate;
    private Integer durationMinutes;
    private String notes;
    private List<ExerciseSummary> exercises;
}
