package com.AdaptFit.SistemaFitness.deterministic.dto.nutritional;

import com.AdaptFit.SistemaFitness.enums.ActivityLevel;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNutritionalPlanRequest {

    @NotNull(message = "Nível de atividade é obrigatório")
    private ActivityLevel activityLevel;
    
    private GoalType goal;
}
