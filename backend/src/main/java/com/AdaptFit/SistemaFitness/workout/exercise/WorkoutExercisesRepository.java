package com.AdaptFit.SistemaFitness.workout.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutExercisesRepository extends JpaRepository<WorkoutExercise, Long> {

    List<WorkoutExercise> findByWorkoutDayId(Long workoutDayId);

    void deleteByWorkoutDayId(Long workoutDayId);
}
