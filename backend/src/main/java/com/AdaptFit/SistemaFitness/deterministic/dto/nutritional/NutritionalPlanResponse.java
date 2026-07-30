package com.AdaptFit.SistemaFitness.deterministic.dto.nutritional;

import com.AdaptFit.SistemaFitness.deterministic.nutricional.NutritionalPlan;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NutritionalPlanResponse {

    private Long id;
    private Double tmb;
    private Double tdee;
    private Double targetCalories;
    private Double proteinGrams;
    private Double carbsGrams;
    private Double fatGrams;
    private GoalType goal;
    private BigDecimal weightKg;
    private Date createdAt;
    private Date updatedAt;

    public static NutritionalPlanResponse fromEntity(NutritionalPlan plan) {
        NutritionalPlanResponse response = new NutritionalPlanResponse();
        response.setId(plan.getId());
        response.setTmb(plan.getTmb());
        response.setTdee(plan.getTdee());
        response.setTargetCalories(plan.getTargetCalories());
        response.setProteinGrams(plan.getProteinGrams());
        response.setCarbsGrams(plan.getCarbsGrams());
        response.setFatGrams(plan.getFatGrams());
        response.setGoal(plan.getGoal());
        response.setWeightKg(plan.getWeightKg());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}
