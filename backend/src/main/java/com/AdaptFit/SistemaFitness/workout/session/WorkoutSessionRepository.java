package com.AdaptFit.SistemaFitness.workout.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    List<WorkoutSession> findByUserIdOrderBySessionDateDesc(Long userId);

    List<WorkoutSession> findByWorkoutDayIdOrderBySessionDateDesc(Long workoutDayId);

    Optional<WorkoutSession> findFirstByUserIdOrderBySessionDateDesc(Long userId);

    @Query("SELECT COUNT(s) FROM WorkoutSession s WHERE s.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(s.durationMinutes) FROM WorkoutSession s WHERE s.userId = :userId")
    Long sumDurationByUserId(@Param("userId") Long userId);

    @Query("SELECT MAX(s.sessionDate) FROM WorkoutSession s WHERE s.userId = :userId")
    Date findLastSessionDateByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT s.sessionDate) FROM WorkoutSession s WHERE s.userId = :userId AND s.sessionDate >= :weekStart")
    Long countSessionsInWeek(@Param("userId") Long userId, @Param("weekStart") Date weekStart);

    @Query("SELECT s FROM WorkoutSession s WHERE s.userId = :userId AND s.sessionDate >= :startDate AND s.sessionDate <= :endDate ORDER BY s.sessionDate DESC")
    List<WorkoutSession> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(s) FROM WorkoutSession s WHERE s.userId = :userId AND s.sessionDate >= :startDate AND s.sessionDate <= :endDate")
    Long countByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(s.durationMinutes) FROM WorkoutSession s WHERE s.userId = :userId AND s.sessionDate >= :startDate AND s.sessionDate <= :endDate")
    Long sumDurationByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT s FROM WorkoutSession s WHERE s.userId = :userId AND s.workoutDayId = :workoutDayId AND s.localDate = :localDate")
    Optional<WorkoutSession> findByUserIdAndWorkoutDayIdAndLocalDate(@Param("userId") Long userId, @Param("workoutDayId") Long workoutDayId, @Param("localDate") LocalDate localDate);

    @Query("SELECT s FROM WorkoutSession s WHERE s.userId = :userId AND s.localDate = :localDate")
    List<WorkoutSession> findByUserIdAndLocalDate(@Param("userId") Long userId, @Param("localDate") LocalDate localDate);

    @Query("SELECT s FROM WorkoutSession s WHERE s.userId = :userId AND s.workoutDayId = :workoutDayId AND s.sessionDate >= :startDate AND s.sessionDate <= :endDate ORDER BY s.sessionDate DESC")
    List<WorkoutSession> findByUserIdAndWorkoutDayIdAndDateRange(@Param("userId") Long userId, @Param("workoutDayId") Long workoutDayId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT s FROM WorkoutSession s WHERE s.userId = :userId AND s.workoutDayId = :workoutDayId ORDER BY s.sessionDate DESC LIMIT 1")
    Optional<WorkoutSession> findLatestByUserIdAndWorkoutDayId(@Param("userId") Long userId, @Param("workoutDayId") Long workoutDayId);
}