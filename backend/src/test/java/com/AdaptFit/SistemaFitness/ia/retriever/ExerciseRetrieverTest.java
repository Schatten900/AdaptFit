package com.AdaptFit.SistemaFitness.ia.retriever;

import com.AdaptFit.SistemaFitness.rag.RagService;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseRetrieverTest {

    @Mock
    private RagService ragService;

    private ExerciseRetriever exerciseRetriever;

    @BeforeEach
    void setUp() {
        exerciseRetriever = new ExerciseRetriever(ragService);
    }

    @Test
    void retrieveExercises_WithEmptyQuery_ShouldReturnEmpty() {
        List<ExerciseCatalog> result = exerciseRetriever.retrieveExercises("", 10);
        assertTrue(result.isEmpty());
    }

    @Test
    void retrieveExercises_WithNullQuery_ShouldReturnEmpty() {
        List<ExerciseCatalog> result = exerciseRetriever.retrieveExercises(null, 10);
        assertTrue(result.isEmpty());
    }

    @Test
    void formatExercises_WithEmptyList_ShouldReturnMessage() {
        String result = exerciseRetriever.formatExercises(Collections.emptyList());
        assertEquals("Nenhum exercício encontrado.", result);
    }

    @Test
    void formatExercises_WithNullList_ShouldReturnMessage() {
        String result = exerciseRetriever.formatExercises(null);
        assertEquals("Nenhum exercício encontrado.", result);
    }

    @Test
    void formatExercises_WithExercises_ShouldReturnFormatted() {
        ExerciseCatalog ex = new ExerciseCatalog();
        ex.setName("Supino Reto");
        ex.setPrimaryMuscle("Peito");
        ex.setIsBodyweight(false);

        String result = exerciseRetriever.formatExercises(List.of(ex));
        assertTrue(result.contains("Supino Reto"));
        assertTrue(result.contains("Peito"));
        assertTrue(result.contains("requer equipamento"));
    }
}
