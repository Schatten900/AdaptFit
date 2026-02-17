package com.AdaptFit.SistemaFitness.workout.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    List<WorkoutSession> findByUserIdOrderBySessionDateDesc(Long userId);

    List<WorkoutSession> findByWorkoutIdOrderBySessionDateDesc(Long workoutId);

    Optional<WorkoutSession> findFirstByUserIdOrderBySessionDateDesc(Long userId);

    @Query("SELECT COUNT(s) FROM WorkoutSession s WHERE s.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(s.durationMinutes) FROM WorkoutSession s WHERE s.userId = :userId")
    Long sumDurationByUserId(@Param("userId") Long userId);

    @Query("SELECT MAX(s.sessionDate) FROM WorkoutSession s WHERE s.userId = :userId")
    Date findLastSessionDateByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT s.sessionDate) FROM WorkoutSession s WHERE s.userId = :userId AND s.sessionDate >= :weekStart")
    Long countSessionsInWeek(@Param("userId") Long userId, @Param("weekStart") Date weekStart);
}