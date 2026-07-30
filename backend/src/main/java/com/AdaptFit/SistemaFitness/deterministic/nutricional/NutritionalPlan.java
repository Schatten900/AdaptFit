package com.AdaptFit.SistemaFitness.deterministic.nutricional;

import com.AdaptFit.SistemaFitness.enums.GoalType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "nutritional_plans")
@Getter
@Setter
public class NutritionalPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tmb", nullable = false)
    private Double tmb;

    @Column(name = "tdee", nullable = false)
    private Double tdee;

    @Column(name = "target_calories", nullable = false)
    private Double targetCalories;

    @Column(name = "protein_grams", nullable = false)
    private Double proteinGrams;

    @Column(name = "carbs_grams", nullable = false)
    private Double carbsGrams;

    @Column(name = "fat_grams", nullable = false)
    private Double fatGrams;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GoalType goal;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
