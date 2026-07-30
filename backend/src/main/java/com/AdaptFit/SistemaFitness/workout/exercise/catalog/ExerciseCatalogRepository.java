package com.AdaptFit.SistemaFitness.workout.exercise.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseCatalogRepository extends JpaRepository<ExerciseCatalog, Long> {

    List<ExerciseCatalog> findByNameContainingIgnoreCase(String name);

    @Query("SELECT e FROM ExerciseCatalog e WHERE e.primaryMuscle = :muscleGroup")
    List<ExerciseCatalog> findByPrimaryMuscle(@Param("muscleGroup") String muscleGroup);

    @Query("SELECT e FROM ExerciseCatalog e WHERE e.name LIKE %:keyword% OR e.description LIKE %:keyword%")
    List<ExerciseCatalog> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT e.primaryMuscle FROM ExerciseCatalog e")
    List<String> findAllMuscleGroups();

    @Query("SELECT e FROM ExerciseCatalog e WHERE e.id IN :ids")
    List<ExerciseCatalog> findByIdIn(@Param("ids") List<Long> ids);
}
