package com.AdaptFit.SistemaFitness.deterministic.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    Optional<WorkoutPlan> findByUserIdAndIsActiveTrue(Long userId);

    Optional<WorkoutPlan> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
