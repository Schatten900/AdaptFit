package com.AdaptFit.SistemaFitness.workout.exercise.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exercise_catalog")
@Getter
@Setter
public class ExerciseCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "primary_muscle", length = 100)
    private String primaryMuscle;

    @Column(name = "secondary_muscles", columnDefinition = "json")
    private String secondaryMuscles;

    @Column(name = "is_bodyweight")
    private Boolean isBodyweight = false;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
