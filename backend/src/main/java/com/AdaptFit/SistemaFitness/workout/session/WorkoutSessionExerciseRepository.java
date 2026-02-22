package com.AdaptFit.SistemaFitness.workout.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutSessionExerciseRepository extends JpaRepository<WorkoutSessionExercise, Long> {

    List<WorkoutSessionExercise> findBySessionId(Long sessionId);

    List<WorkoutSessionExercise> findTop10ByExerciseIdAndUserIdOrderBySessionDateDesc(Long exerciseId, Long userId);

    List<WorkoutSessionExercise> findByUserIdAndSessionDateBetweenOrderBySessionDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    List<WorkoutSessionExercise> findByExerciseIdAndUserIdOrderBySessionDateDesc(Long exerciseId, Long userId);
}
