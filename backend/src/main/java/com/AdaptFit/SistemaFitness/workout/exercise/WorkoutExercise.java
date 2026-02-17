package com.AdaptFit.SistemaFitness.workout.exercise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercise_id", nullable = false)
    private Long exerciseId;

    @Column(name = "workout_day_id", nullable = false)
    private Long workoutDayId;

    private Integer sets;
    private Integer reps;

    @Column(name = "rest_time_seconds")
    private Integer restTimeSeconds;

    @Column(precision = 8, scale = 2)
    private BigDecimal weight;

    @Column(name = "exercise_order")
    private Integer exerciseOrder;
}
