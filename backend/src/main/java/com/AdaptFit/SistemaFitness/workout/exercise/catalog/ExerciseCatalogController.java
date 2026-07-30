package com.AdaptFit.SistemaFitness.workout.exercise.catalog;

import com.AdaptFit.SistemaFitness.workout.dto.Catalog.ExerciseCatalogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseCatalogController {

    private final ExerciseCatalogService exerciseCatalogService;

    @GetMapping
    public ResponseEntity<List<ExerciseCatalogResponse>> getAllExercises() {
        List<ExerciseCatalogResponse> responses = exerciseCatalogService.getAllExercises().stream()
                .map(ExerciseCatalogResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
