package com.AdaptFit.SistemaFitness.profile;

import com.AdaptFit.SistemaFitness.enums.ExperienceLevel;
import com.AdaptFit.SistemaFitness.enums.Gender;
import com.AdaptFit.SistemaFitness.enums.GoalType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @NotNull
    @Min(1)
    private Integer age;

    @NotNull
    @Min(1)
    @Column(precision = 5, scale = 2)
    private BigDecimal height;

    @NotNull
    @Min(1)
    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GoalType goal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ExperienceLevel experience;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonProperty("daysPerWeek")
    @Column(name = "days_per_week")
    private int daysPerWeek;

    @JsonProperty("sessionDuration")
    @Column(name = "session_duration")
    private int sessionDuration;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
