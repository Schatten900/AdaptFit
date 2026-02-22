package com.AdaptFit.SistemaFitness.workout.dto.Session;

import com.AdaptFit.SistemaFitness.workout.dto.WorkoutDay.WorkoutDayResponse;
import lombok.Data;

@Data
public class TodayStatusResponse {
    private String status;
    private WorkoutDayResponse workoutDay;
    private WorkoutSessionResponse completedSession;
    private String message;

    public static final String STATUS_WORKOUT = "WORKOUT";
    public static final String STATUS_REST = "REST";
    public static final String STATUS_COMPLETED = "COMPLETED";
}
