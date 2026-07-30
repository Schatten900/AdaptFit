package com.AdaptFit.SistemaFitness.deterministic.nutricional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionalPlanRepository extends JpaRepository<NutritionalPlan, Long> {

    Optional<NutritionalPlan> findByUserIdAndIsActiveTrue(Long userId);

    Optional<NutritionalPlan> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
