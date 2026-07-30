package com.AdaptFit.SistemaFitness.deterministic.evolution;

import com.AdaptFit.SistemaFitness.enums.AdjustmentReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EvolutionLogRepository extends JpaRepository<EvolutionLog, Long> {

    List<EvolutionLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<EvolutionLog> findByUserIdAndParameterNameOrderByCreatedAtDesc(Long userId, String parameterName);

    @Query("SELECT e FROM EvolutionLog e WHERE e.userId = :userId AND e.createdAt >= :startDate ORDER BY e.createdAt DESC")
    List<EvolutionLog> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate);

    List<EvolutionLog> findByUserIdAndAdjustmentReasonOrderByCreatedAtDesc(Long userId, AdjustmentReason reason);

    @Query("SELECT COUNT(e) FROM EvolutionLog e WHERE e.userId = :userId AND e.parameterName = :parameterName AND e.createdAt >= :startDate")
    Long countByUserIdAndParameterNameSince(@Param("userId") Long userId, @Param("parameterName") String parameterName, @Param("startDate") Date startDate);
}
