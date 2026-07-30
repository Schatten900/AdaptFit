package com.AdaptFit.SistemaFitness.deterministic.dto.workout;

import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutPlan;
import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import com.AdaptFit.SistemaFitness.enums.WorkoutSplit;
import lombok.Data;

import java.util.Date;

@Data
public class WorkoutPlanResponse {

    private Long id;
    private WorkoutSplit workoutSplit;
    private ExperienceLevel experienceLevel;
    private Integer daysPerWeek;
    private Date createdAt;
    private Date updatedAt;

    public static WorkoutPlanResponse fromEntity(WorkoutPlan plan) {
        WorkoutPlanResponse response = new WorkoutPlanResponse();
        response.setId(plan.getId());
        response.setWorkoutSplit(plan.getWorkoutSplit());
        response.setExperienceLevel(plan.getExperienceLevel());
        response.setDaysPerWeek(plan.getDaysPerWeek());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}
