package com.AdaptFit.SistemaFitness.ia.context;

import com.AdaptFit.SistemaFitness.deterministic.nutricional.NutritionalPlan;
import com.AdaptFit.SistemaFitness.preferences.UserPreferences;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NutritionContext {
    private Profile profile;
    private UserPreferences preferences;
    private NutritionalPlan nutritionalPlan;
    private List<Recipe> ragDocuments;
    private String userQuestion;
}
