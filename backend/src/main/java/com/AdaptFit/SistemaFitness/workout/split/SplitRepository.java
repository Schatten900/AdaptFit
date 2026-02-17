package com.AdaptFit.SistemaFitness.workout.split;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SplitRepository extends JpaRepository<Split, Long> {

    List<Split> findByUserId(Long userId);

    List<Split> findByUserIdAndActiveTrue(Long userId);

    Optional<Split> findByIdAndUserId(Long id, Long userId);
}
