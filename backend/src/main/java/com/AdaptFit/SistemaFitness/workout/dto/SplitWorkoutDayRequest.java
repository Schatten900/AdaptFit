package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

@Data
public class SplitWorkoutDayRequest {

    private Long workoutDayId;
    private Integer dayOfWeek;
    private Integer dayOrder;
}
