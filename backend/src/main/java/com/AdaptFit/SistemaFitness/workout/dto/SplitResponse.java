package com.AdaptFit.SistemaFitness.workout.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SplitResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private List<SplitWorkoutDayResponse> splitWorkoutDays;
    private Date createdAt;
    private Date updatedAt;
}
