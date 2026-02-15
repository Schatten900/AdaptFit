package com.AdaptFit.SistemaFitness.workout.exercise;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseCatalogRepository exerciseRepository;

    @GetMapping
    public ResponseEntity<List<ExerciseCatalog>> getAllExercises() {
        return ResponseEntity.ok(exerciseRepository.findAll());
    }
}


