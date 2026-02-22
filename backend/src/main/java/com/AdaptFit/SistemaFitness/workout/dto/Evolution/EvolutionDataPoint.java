package com.AdaptFit.SistemaFitness.workout.dto.Evolution;

import lombok.Data;
import java.util.Date;

@Data
public class EvolutionDataPoint {
    private Date date;
    private Integer sessionsCount;
    private Integer totalDuration;
    private Integer totalReps;
    private Double totalWeight;
    private Double totalVolume;
    private String period;
}
