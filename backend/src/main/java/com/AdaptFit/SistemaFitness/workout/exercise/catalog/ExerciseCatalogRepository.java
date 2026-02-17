package com.AdaptFit.SistemaFitness.workout.exercise.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseCatalogRepository extends JpaRepository<ExerciseCatalog, Long> {

}

