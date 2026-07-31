package com.AdaptFit.SistemaFitness.ia.retriever;

import com.AdaptFit.SistemaFitness.enums.DietType;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.enums.MealType;
import com.AdaptFit.SistemaFitness.rag.RagService;
import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import com.AdaptFit.SistemaFitness.rag.recipe.RecipeSearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeRetriever {

    private final RagService ragService;

    public List<Recipe> retrieveRecipes(String keyword, MealType mealType, GoalType goal, int limit) {
        RecipeSearchQuery searchQuery = new RecipeSearchQuery();
        searchQuery.setKeyword(keyword);
        searchQuery.setMealType(mealType);
        searchQuery.setObjective(goal);
        searchQuery.setLimit(limit);

        List<Recipe> results = ragService.searchRecipes(searchQuery);
        log.info("Recipe RAG retrieved {} results", results.size());
        return results;
    }

    public List<Recipe> retrieveByGoal(GoalType goal, int limit) {
        return retrieveRecipes(null, null, goal, limit);
    }

    public List<Recipe> retrieveByDiet(DietType diet, int limit) {
        RecipeSearchQuery searchQuery = new RecipeSearchQuery();
        searchQuery.setDiet(diet);
        searchQuery.setLimit(limit);

        List<Recipe> results = ragService.searchRecipes(searchQuery);
        log.info("Recipe RAG retrieved {} results for diet: {}", results.size(), diet);
        return results;
    }

    public List<Recipe> retrieveByMealType(MealType mealType, int limit) {
        return retrieveRecipes(null, mealType, null, limit);
    }

    public String formatRecipes(List<Recipe> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            return "Nenhuma receita encontrada.";
        }
        return recipes.stream()
                .map(r -> String.format("- %s (calorias: %d, proteína: %.1fg, carboidratos: %.1fg, gorduras: %.1fg)",
                        r.getNome(),
                        r.getCaloriesPorPorcao() != null ? r.getCaloriesPorPorcao() : 0,
                        r.getProteinaG() != null ? r.getProteinaG() : 0,
                        r.getCarboidratosG() != null ? r.getCarboidratosG() : 0,
                        r.getGordurasG() != null ? r.getGordurasG() : 0))
                .collect(Collectors.joining("\n"));
    }
}
