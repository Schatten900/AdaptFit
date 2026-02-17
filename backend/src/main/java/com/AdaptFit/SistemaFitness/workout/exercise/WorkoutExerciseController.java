package com.AdaptFit.SistemaFitness.workout.exercise;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workout-exercises")
@RequiredArgsConstructor
public class WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    @GetMapping("/workout-day/{workoutDayId}")
    public ResponseEntity<List<WorkoutExercise>> getByWorkoutDay(
            @PathVariable Long workoutDayId
    ) {
        return ResponseEntity.ok(
                workoutExerciseService.findExercisesByWorkoutId(workoutDayId)
        );
    }
}