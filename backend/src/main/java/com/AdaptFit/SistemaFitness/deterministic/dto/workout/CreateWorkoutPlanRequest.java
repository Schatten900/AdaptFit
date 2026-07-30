package com.AdaptFit.SistemaFitness.deterministic.dto.workout;

import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWorkoutPlanRequest {

    @NotNull(message = "Nível de experiência é obrigatório")
    private ExperienceLevel experienceLevel;
    
    @NotNull(message = "Dias disponíveis por semana é obrigatório")
    @Min(value = 2, message = "Mínimo de 2 dias por semana")
    @Max(value = 7, message = "Máximo de 7 dias por semana")
    private Integer availableDays;
}
