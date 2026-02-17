package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateSplitRequest {

    private String name;
    private String description;
    private Boolean active;
    private List<SplitWorkoutDayRequest> splitWorkoutDays;
}
