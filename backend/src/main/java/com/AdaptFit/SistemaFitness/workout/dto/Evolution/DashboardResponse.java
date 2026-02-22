package com.AdaptFit.SistemaFitness.workout.dto.Evolution;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardResponse {
    private Integer totalSessions;
    private Integer totalDurationMinutes;
    private Double averageDuration;
    private Integer totalReps;
    private Double totalWeight;
    private Double totalVolume;
    private Map<String, Long> sessionsByMuscleGroup;
    private List<EvolutionDataPoint> evolutionData;
    private Map<String, Long> workoutDistribution;
}
