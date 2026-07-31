package com.AdaptFit.SistemaFitness.rag;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.enums.DietType;
import com.AdaptFit.SistemaFitness.enums.Equipment;
import com.AdaptFit.SistemaFitness.enums.ExerciseLevel;
import com.AdaptFit.SistemaFitness.enums.ExerciseType;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.AdaptFit.SistemaFitness.enums.MealType;
import com.AdaptFit.SistemaFitness.rag.dto.ExerciseResponse;
import com.AdaptFit.SistemaFitness.rag.dto.RecipeResponse;
import com.AdaptFit.SistemaFitness.rag.exercise.ExerciseSearchQuery;
import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import com.AdaptFit.SistemaFitness.rag.recipe.RecipeSearchQuery;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;
    private final QdrantSyncService qdrantSyncService;

    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<List<RecipeResponse>>> searchRecipes(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) MealType mealType,
            @RequestParam(required = false) DietType diet,
            @RequestParam(required = false) GoalType objective,
            @RequestParam(required = false) List<String> excludeAllergens,
            @RequestParam(required = false) Integer minCalories,
            @RequestParam(required = false) Integer maxCalories,
            @RequestParam(required = false) Double minProtein,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {

        RecipeSearchQuery query = new RecipeSearchQuery();
        query.setKeyword(keyword);
        query.setMealType(mealType);
        query.setDiet(diet);
        query.setObjective(objective);
        query.setExcludeAllergens(excludeAllergens);
        query.setMinCalories(minCalories);
        query.setMaxCalories(maxCalories);
        query.setMinProtein(minProtein);
        query.setLimit(limit);

        List<Recipe> recipes = ragService.searchRecipes(query);
        List<RecipeResponse> responses = recipes.stream()
                .map(RecipeResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/exercises")
    public ResponseEntity<ApiResponse<List<ExerciseResponse>>> searchExercises(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) ExerciseLevel level,
            @RequestParam(required = false) Equipment equipment,
            @RequestParam(required = false) ExerciseType type,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {

        ExerciseSearchQuery query = new ExerciseSearchQuery();
        query.setKeyword(keyword);
        query.setMuscleGroup(muscleGroup);
        query.setLevel(level);
        query.setEquipment(equipment);
        query.setType(type);
        query.setLimit(limit);

        List<ExerciseCatalog> exercises = ragService.searchExercises(query);
        List<ExerciseResponse> responses = exercises.stream()
                .map(ExerciseResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PostMapping("/reindex")
    public ResponseEntity<ApiResponse<String>> reindex() {
        qdrantSyncService.syncDataToQdrant(true);
        return ResponseEntity.ok(ApiResponse.success("Reindexação concluída"));
    }

    @GetMapping("/muscle-groups")
    public ResponseEntity<ApiResponse<List<String>>> getAllMuscleGroups() {
        List<String> muscleGroups = ragService.getAllMuscleGroups();
        return ResponseEntity.ok(ApiResponse.success(muscleGroups));
    }
}
