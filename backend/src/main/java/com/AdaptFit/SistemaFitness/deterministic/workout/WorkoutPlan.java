package com.AdaptFit.SistemaFitness.deterministic.workout;

import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import com.AdaptFit.SistemaFitness.enums.WorkoutSplit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "workout_plans")
@Getter
@Setter
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_split", length = 20)
    private WorkoutSplit workoutSplit;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", length = 20)
    private ExperienceLevel experienceLevel;

    @Column(name = "days_per_week", nullable = false)
    private Integer daysPerWeek;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "volume_multiplier", precision = 3)
    private Double volumeMultiplier;

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
