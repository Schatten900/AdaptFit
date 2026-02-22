package com.AdaptFit.SistemaFitness.workout.dto.Session;

import lombok.Data;

@Data
public class SetPerformedRequest {
    private Integer setNumber;
    private Integer reps;
    private Double weight;
    private Boolean done;
}
