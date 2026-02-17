package com.AdaptFit.SistemaFitness.workout.day;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, Long> {

    List<WorkoutDay> findByUserId(Long userId);

    List<WorkoutDay> findByUserIdAndWorkoutIdIsNull(Long userId);

    List<WorkoutDay> findByWorkoutId(Long workoutId);

    Optional<WorkoutDay> findByIdAndUserId(Long id, Long userId);
}
