package com.AdaptFit.SistemaFitness.workout.dto.WorkoutDay;

import com.AdaptFit.SistemaFitness.workout.dto.Exercise.WorkoutExerciseRequest;
import lombok.Data;

import java.util.List;

@Data
public class CreateWorkoutDayRequest {

    private String name;
    private String description;
    private List<WorkoutExerciseRequest> exercises;
}
