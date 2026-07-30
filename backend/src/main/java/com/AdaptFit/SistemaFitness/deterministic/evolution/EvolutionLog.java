package com.AdaptFit.SistemaFitness.deterministic.evolution;

import com.AdaptFit.SistemaFitness.enums.AdjustmentReason;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Entity
@Table(name = "evolution_logs")
@Getter
@Setter
public class EvolutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "adjustment_reason", length = 30)
    private AdjustmentReason adjustmentReason;

    @Column(name = "previous_value")
    private Double previousValue;

    @Column(name = "new_value")
    private Double newValue;

    @Column(name = "parameter_name", length = 50)
    private String parameterName;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
