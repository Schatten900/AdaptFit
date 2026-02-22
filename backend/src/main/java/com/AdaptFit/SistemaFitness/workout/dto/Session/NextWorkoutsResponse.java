package com.AdaptFit.SistemaFitness.workout.dto.Session;

import com.AdaptFit.SistemaFitness.workout.dto.WorkoutDay.WorkoutDayResponse;
import lombok.Data;

import java.util.List;

@Data
public class NextWorkoutsResponse {
    private WorkoutDayResponse todayWorkout;
    private String todayStatus;
    private List<WorkoutDayResponse> nextWorkouts;
    private Integer currentDayOfWeek;
}
