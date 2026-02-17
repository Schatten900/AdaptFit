package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateSplitRequest {
    private String name;
    private String description;
    private List<SplitWorkoutDayRequest> splitWorkoutDays;
}
