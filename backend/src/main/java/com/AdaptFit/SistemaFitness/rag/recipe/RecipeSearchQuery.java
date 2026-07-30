package com.AdaptFit.SistemaFitness.rag.recipe;

import com.AdaptFit.SistemaFitness.enums.DietType;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.enums.MealType;
import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchQuery {
    private String keyword;
    private MealType mealType;
    private DietType diet;
    private GoalType objective;
    private List<String> excludeAllergens;
    private Integer minCalories;
    private Integer maxCalories;
    private Double minProtein;
    private Integer limit;
}
