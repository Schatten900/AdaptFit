package com.AdaptFit.SistemaFitness.workout.exercise;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {
    private final WorkoutExercisesRepository workoutExercisesRepository;;

    public List<WorkoutExercise> findExercisesByWorkoutId(Long workoutExerciseId){
        List<WorkoutExercise> workoutList =  workoutExercisesRepository.findByWorkoutDayId(workoutExerciseId);
        if (workoutList == null || workoutList.isEmpty()){
            throw new NotFoundException("Exercícios não encontrados para este treino");
        }
        return workoutList;
    }
}
