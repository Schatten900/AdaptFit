package com.AdaptFit.SistemaFitness.workout.dto.Session;

import lombok.Data;

@Data
public class SetPerformedResponse {
    private Integer setNumber;
    private Integer reps;
    private Double weight;
    private Double volume;
}
