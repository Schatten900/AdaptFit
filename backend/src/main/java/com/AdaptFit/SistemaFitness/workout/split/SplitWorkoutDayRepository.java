package com.AdaptFit.SistemaFitness.workout.split;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitWorkoutDayRepository extends JpaRepository<SplitWorkoutDay, Long> {

    List<SplitWorkoutDay> findBySplitId(Long splitId);

    List<SplitWorkoutDay> findBySplitIdOrderByDayOrder(Long splitId);

    void deleteBySplitId(Long splitId);
}

