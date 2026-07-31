package com.AdaptFit.SistemaFitness.rag;

import com.AdaptFit.SistemaFitness.rag.config.QdrantService;
import com.AdaptFit.SistemaFitness.rag.embedding.EmbeddingService;
import com.AdaptFit.SistemaFitness.rag.recipe.Recipe;
import com.AdaptFit.SistemaFitness.rag.recipe.RecipeRepository;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Async;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QdrantSyncService {

    private final QdrantService qdrantService;
    private final EmbeddingService embeddingService;
    private final ExerciseCatalogRepository exerciseCatalogRepository;
    private final RecipeRepository recipeRepository;

    @Value("${rag.qdrant.enabled:false}")
    private boolean qdrantEnabled;

    @PostConstruct
    public void syncDataToQdrant() {
        asyncSync();
    }

    @Async
    public void asyncSync() {
        syncDataToQdrant(false);
    }

    public void syncDataToQdrant(boolean forceDelete) {
        if (!qdrantEnabled) {
            log.info("Qdrant sync disabled, skipping...");
            return;
        }

        if (!embeddingService.isAvailable()) {
            log.warn("Ollama not available, skipping Qdrant sync...");
            return;
        }

        log.info("Starting Qdrant sync...");

        if (forceDelete) {
            qdrantService.deleteCollection(QdrantService.EXERCISES_COLLECTION);
            qdrantService.deleteCollection(QdrantService.RECIPES_COLLECTION);
        }
        
        try {
            syncExercises();
            syncRecipes();
            log.info("Qdrant sync completed successfully");
        } catch (Exception e) {
            log.error("Error during Qdrant sync: {}", e.getMessage(), e);
        }
    }

    private void syncExercises() {
        try {
            qdrantService.createCollectionIfNotExists(QdrantService.EXERCISES_COLLECTION, QdrantService.VECTOR_SIZE);

            List<ExerciseCatalog> exercises = exerciseCatalogRepository.findAll();

            if (exercises.isEmpty()) {
                log.warn("No exercises found in database to sync to Qdrant");
                return;
            }

            List<Map<String, Object>> points = new ArrayList<>();

            for (ExerciseCatalog exercise : exercises) {
                Long id = exercise.getId();
                String nome = exercise.getName();
                String musculoPrincipal = exercise.getPrimaryMuscle() != null ? exercise.getPrimaryMuscle() : "";
                String descricao = exercise.getDescription() != null ? exercise.getDescription() : "";

                String textToEmbed = nome + " " + musculoPrincipal + " " + descricao;
                float[] embedding = embeddingService.generateEmbedding(textToEmbed);

                if (embedding != null) {
                    Map<String, Object> point = new HashMap<>();
                    point.put("id", id);
                    point.put("vector", embedding);
                    
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("nome", nome);
                    payload.put("musculo_principal", musculoPrincipal);
                    payload.put("descricao", descricao);
                    point.put("payload", payload);
                    
                    points.add(point);
                }
            }

            if (!points.isEmpty()) {
                qdrantService.upsertPoints(QdrantService.EXERCISES_COLLECTION, points);
                log.info("Synced {} exercises to Qdrant", points.size());
            }

        } catch (Exception e) {
            log.error("Error syncing exercises: {}", e.getMessage(), e);
        }
    }

    private void syncRecipes() {
        try {
            qdrantService.createCollectionIfNotExists(QdrantService.RECIPES_COLLECTION, QdrantService.VECTOR_SIZE);

            List<Recipe> recipes = recipeRepository.findAll();

            if (recipes.isEmpty()) {
                log.warn("No recipes found in database to sync to Qdrant");
                return;
            }

            List<Map<String, Object>> points = new ArrayList<>();

            for (Recipe recipe : recipes) {
                Long id = recipe.getId();
                String nome = recipe.getNome();
                
                StringBuilder ingredientes = new StringBuilder();
                if (recipe.getIngredientes() != null) {
                    for (String ingrediente : recipe.getIngredientes()) {
                        ingredientes.append(ingrediente).append(" ");
                    }
                }
                
                String textToEmbed = nome + " " + ingredientes.toString();
                float[] embedding = embeddingService.generateEmbedding(textToEmbed);

                if (embedding != null) {
                    Map<String, Object> point = new HashMap<>();
                    point.put("id", id);
                    point.put("vector", embedding);
                    
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("nome", nome);
                    payload.put("ingredientes", ingredientes.toString().trim());
                    
                    if (recipe.getCaloriesPorPorcao() != null) {
                        payload.put("calorias", recipe.getCaloriesPorPorcao());
                    }
                    if (recipe.getProteinaG() != null) {
                        payload.put("proteina", recipe.getProteinaG());
                    }
                    point.put("payload", payload);
                    
                    points.add(point);
                }
            }

            if (!points.isEmpty()) {
                qdrantService.upsertPoints(QdrantService.RECIPES_COLLECTION, points);
                log.info("Synced {} recipes to Qdrant", points.size());
            }

        } catch (Exception e) {
            log.error("Error syncing recipes: {}", e.getMessage(), e);
        }
    }
}
