package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

@Data
public class SplitWorkoutDayResponse {

    private Long id;
    private Long splitId;
    private Long workoutDayId;
    private String workoutDayName;
    private Integer dayOfWeek;
    private Integer dayOrder;
}
