package com.AdaptFit.SistemaFitness.rag;

import com.AdaptFit.SistemaFitness.enums.*;
import com.AdaptFit.SistemaFitness.rag.config.QdrantService;
import com.AdaptFit.SistemaFitness.rag.embedding.EmbeddingService;
import com.AdaptFit.SistemaFitness.rag.exercise.ExerciseSearchQuery;
import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import com.AdaptFit.SistemaFitness.rag.recipe.RecipeRepository;
import com.AdaptFit.SistemaFitness.rag.recipe.RecipeSearchQuery;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagService {

    private final RecipeRepository recipeRepository;
    private final ExerciseCatalogRepository exerciseRepository;
    private final QdrantService qdrantService;
    private final EmbeddingService embeddingService;

    @Value("${rag.qdrant.enabled:false}")
    private boolean qdrantEnabled;

    private static final Map<MealType, String> MEAL_TYPE_PT = Map.ofEntries(
            Map.entry(MealType.BREAKFAST, "cafe da manha"),
            Map.entry(MealType.LUNCH, "almoco"),
            Map.entry(MealType.DINNER, "jantar"),
            Map.entry(MealType.SNACK, "lanche"),
            Map.entry(MealType.PRE_WORKOUT, "pre treino"),
            Map.entry(MealType.POST_WORKOUT, "pos treino"),
            Map.entry(MealType.SIDE_DISH, "acompanhamento"),
            Map.entry(MealType.DESSERT, "sobremesa"),
            Map.entry(MealType.APPETIZER, "entrada"),
            Map.entry(MealType.HEALTHY_SNACK, "petisco saudavel"),
            Map.entry(MealType.BEVERAGE, "bebida"),
            Map.entry(MealType.LIGHT_MEAL, "jantar leve"),
            Map.entry(MealType.POST_WORKOUT_SNACK, "lanche pos treino"),
            Map.entry(MealType.QUICK_LUNCH, "almoco rapido"),
            Map.entry(MealType.QUICK_MEAL, "jantar rapido")
    );

    private static final Map<DietType, String> DIET_TYPE_PT = Map.ofEntries(
            Map.entry(DietType.OMNIVORE, "onivoro"),
            Map.entry(DietType.VEGETARIAN, "vegetariano"),
            Map.entry(DietType.VEGAN, "vegano"),
            Map.entry(DietType.LOW_CARB, "low carb"),
            Map.entry(DietType.HIGH_PROTEIN, "alta proteina"),
            Map.entry(DietType.LOW_CALORIE, "baixa caloria"),
            Map.entry(DietType.KETOGENIC, "cetogenica"),
            Map.entry(DietType.DETOX, "detox"),
            Map.entry(DietType.BALANCED, "equilibrada"),
            Map.entry(DietType.PALEO, "paleo"),
            Map.entry(DietType.PESCATARIAN, "pescetariano"),
            Map.entry(DietType.HIGH_FIBER, "rico em fibras"),
            Map.entry(DietType.GLUTEN_FREE, "sem gluten"),
            Map.entry(DietType.DAIRY_FREE, "sem lactose"),
            Map.entry(DietType.ALCOHOL_FREE, "sem alcool"),
            Map.entry(DietType.OMEGA_3, "omega 3"),
            Map.entry(DietType.PRACTICAL, "pratico"),
            Map.entry(DietType.PRE_WORKOUT, "pre treino"),
            Map.entry(DietType.POST_WORKOUT, "pos treino")
    );

    private static final Map<GoalType, String> GOAL_TYPE_PT = Map.of(
            GoalType.FAT_LOSS, "perda de gordura",
            GoalType.MUSCLE_GAIN, "ganho de massa",
            GoalType.ENDURANCE, "resistencia",
            GoalType.STRENGTH, "forca"
    );

    private static final Map<ExerciseLevel, String> LEVEL_PT = Map.of(
            ExerciseLevel.BEGINNER, "iniciante",
            ExerciseLevel.INTERMEDIATE, "intermediario",
            ExerciseLevel.ADVANCED, "avancado"
    );

    private static final Map<Equipment, String> EQUIPMENT_PT = Map.ofEntries(
            Map.entry(Equipment.DUMBBELL, "halteres"),
            Map.entry(Equipment.BARBELL, "barra"),
            Map.entry(Equipment.MACHINE, "maquina"),
            Map.entry(Equipment.BODYWEIGHT, "peso corporal"),
            Map.entry(Equipment.KETTLEBELL, "kettlebell"),
            Map.entry(Equipment.CABLE, "cabo"),
            Map.entry(Equipment.BAND, "elastico"),
            Map.entry(Equipment.BIKE, "bicicleta"),
            Map.entry(Equipment.TREADMILL, "esteira")
    );

    private static final Map<ExerciseType, String> EXERCISE_TYPE_PT = Map.of(
            ExerciseType.PUSH, "empurrar",
            ExerciseType.PULL, "puxar",
            ExerciseType.LEGS, "pernas",
            ExerciseType.CORE, "core"
    );

    private boolean isQdrantAvailable() {
        if (!qdrantEnabled) {
            return false;
        }
        try {
            return qdrantService.collectionExists(QdrantService.RECIPES_COLLECTION);
        } catch (Exception e) {
            log.warn("Qdrant service unavailable: {}", e.getMessage());
            return false;
        }
    }

    public List<Recipe> searchRecipes(RecipeSearchQuery query) {
        log.info("Searching recipes with query: {}", query);

        if (!isQdrantAvailable() || !embeddingService.isAvailable()) {
            log.warn("Qdrant unavailable, returning empty results for recipe search");
            return Collections.emptyList();
        }

        return searchRecipesWithQdrant(query);
    }

    public List<ExerciseCatalog> searchExercises(ExerciseSearchQuery query) {
        log.info("Searching exercises with query: {}", query);

        if (!isQdrantAvailable() || !embeddingService.isAvailable()) {
            log.warn("Qdrant unavailable, returning empty results for exercise search");
            return Collections.emptyList();
        }

        return searchExercisesWithQdrant(query);
    }

    private List<Recipe> searchRecipesWithQdrant(RecipeSearchQuery query) {
        try {
            String searchText = buildRecipeSearchText(query);
            float[] embedding = embeddingService.generateEmbedding(searchText);

            if (embedding == null) {
                log.warn("Failed to generate embedding for recipe search");
                return Collections.emptyList();
            }

            int limit = query.getLimit() != null ? query.getLimit() : 20;
            List<Map<String, Object>> results = qdrantService.search(
                    QdrantService.RECIPES_COLLECTION,
                    embedding,
                    limit
            );

            if (results == null || results.isEmpty()) {
                return Collections.emptyList();
            }

            List<String> recipeIds = new ArrayList<>();
            for (Map<String, Object> result : results) {
                Object idObj = result.get("id");
                if (idObj != null) {
                    recipeIds.add(idObj.toString());
                }
            }

            return recipeRepository.findByRecipeIdIn(recipeIds);

        } catch (Exception e) {
            log.error("Error searching recipes with Qdrant: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<ExerciseCatalog> searchExercisesWithQdrant(ExerciseSearchQuery query) {
        try {
            String searchText = buildExerciseSearchText(query);
            float[] embedding = embeddingService.generateEmbedding(searchText);

            if (embedding == null) {
                log.warn("Failed to generate embedding for exercise search");
                return Collections.emptyList();
            }

            int limit = query.getLimit() != null ? query.getLimit() : 20;
            List<Map<String, Object>> results = qdrantService.search(
                    QdrantService.EXERCISES_COLLECTION,
                    embedding,
                    limit
            );

            if (results == null || results.isEmpty()) {
                return Collections.emptyList();
            }

            List<Long> exerciseIds = new ArrayList<>();
            for (Map<String, Object> result : results) {
                Object idObj = result.get("id");
                if (idObj != null) {
                    try {
                        exerciseIds.add(Long.parseLong(idObj.toString()));
                    } catch (NumberFormatException e) {
                        log.warn("Could not parse exercise ID: {}", idObj);
                    }
                }
            }

            if (exerciseIds.isEmpty()) {
                return Collections.emptyList();
            }

            return exerciseRepository.findByIdIn(exerciseIds);

        } catch (Exception e) {
            log.error("Error searching exercises with Qdrant: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String buildRecipeSearchText(RecipeSearchQuery query) {
        StringBuilder text = new StringBuilder();

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            text.append(query.getKeyword()).append(" ");
        }

        if (query.getMealType() != null) {
            text.append(MEAL_TYPE_PT.getOrDefault(query.getMealType(), query.getMealType().name())).append(" ");
        }

        if (query.getDiet() != null) {
            text.append(DIET_TYPE_PT.getOrDefault(query.getDiet(), query.getDiet().name())).append(" ");
        }

        if (query.getObjective() != null) {
            text.append(GOAL_TYPE_PT.getOrDefault(query.getObjective(), query.getObjective().name())).append(" ");
        }

        return text.toString().trim();
    }

    private String buildExerciseSearchText(ExerciseSearchQuery query) {
        StringBuilder text = new StringBuilder();

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            text.append(query.getKeyword()).append(" ");
        }

        if (query.getMuscleGroup() != null && !query.getMuscleGroup().isEmpty()) {
            text.append(query.getMuscleGroup()).append(" ");
        }

        if (query.getLevel() != null) {
            text.append(LEVEL_PT.getOrDefault(query.getLevel(), query.getLevel().name())).append(" ");
        }

        if (query.getEquipment() != null) {
            text.append(EQUIPMENT_PT.getOrDefault(query.getEquipment(), query.getEquipment().name())).append(" ");
        }

        if (query.getType() != null) {
            text.append(EXERCISE_TYPE_PT.getOrDefault(query.getType(), query.getType().name())).append(" ");
        }

        return text.toString().trim();
    }

    public List<String> getAllMuscleGroups() {
        return exerciseRepository.findAllMuscleGroups();
    }
}
