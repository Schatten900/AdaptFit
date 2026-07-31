package com.AdaptFit.SistemaFitness.ia.retriever;

import com.AdaptFit.SistemaFitness.rag.RagService;
import com.AdaptFit.SistemaFitness.rag.exercise.ExerciseSearchQuery;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseRetriever {

    private final RagService ragService;

    public List<ExerciseCatalog> retrieveExercises(String query, int limit) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        ExerciseSearchQuery searchQuery = new ExerciseSearchQuery();
        searchQuery.setKeyword(query);
        searchQuery.setLimit(limit);

        List<ExerciseCatalog> results = ragService.searchExercises(searchQuery);
        log.info("Exercise RAG retrieved {} results for query: {}", results.size(), query);
        return results;
    }

    public List<ExerciseCatalog> retrieveByMuscle(String muscleGroup, int limit) {
        ExerciseSearchQuery searchQuery = new ExerciseSearchQuery();
        searchQuery.setMuscleGroup(muscleGroup);
        searchQuery.setLimit(limit);

        List<ExerciseCatalog> results = ragService.searchExercises(searchQuery);
        log.info("Exercise RAG retrieved {} results for muscle: {}", results.size(), muscleGroup);
        return results;
    }

    public String formatExercises(List<ExerciseCatalog> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            return "Nenhum exercício encontrado.";
        }
        return exercises.stream()
                .map(ex -> String.format("- %s (músculo: %s, %s)",
                        ex.getName(),
                        ex.getPrimaryMuscle(),
                        ex.getIsBodyweight() != null && ex.getIsBodyweight() ? "peso corporal" : "requer equipamento"))
                .collect(Collectors.joining("\n"));
    }
}
