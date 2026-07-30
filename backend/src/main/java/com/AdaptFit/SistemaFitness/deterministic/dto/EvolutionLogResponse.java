package com.AdaptFit.SistemaFitness.deterministic.dto;

import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.enums.AdjustmentReason;
import lombok.Data;

import java.util.Date;

@Data
public class EvolutionLogResponse {

    private Long id;
    private AdjustmentReason adjustmentReason;
    private Double previousValue;
    private Double newValue;
    private String parameterName;
    private String description;
    private Date createdAt;

    public static EvolutionLogResponse fromEntity(EvolutionLog log) {
        EvolutionLogResponse response = new EvolutionLogResponse();
        response.setId(log.getId());
        response.setAdjustmentReason(log.getAdjustmentReason());
        response.setPreviousValue(log.getPreviousValue());
        response.setNewValue(log.getNewValue());
        response.setParameterName(log.getParameterName());
        response.setDescription(log.getDescription());
        response.setCreatedAt(log.getCreatedAt());
        return response;
    }
}
