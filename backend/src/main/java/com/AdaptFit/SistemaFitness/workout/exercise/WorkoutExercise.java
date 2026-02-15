package com.AdaptFit.SistemaFitness.workout.exercise;

import com.AdaptFit.SistemaFitness.workout.day.WorkoutDay;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_day_id", nullable = false)
    private WorkoutDay workoutDay;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column
    private Integer sets;

    @Column
    private Integer reps;

    @Column
    private Double weight;

    @Column(name = "rest_time_seconds")
    private Integer restTimeSeconds;

    // Order in the day
    @Column(name = "exercise_order")
    private Integer exerciseOrder;
}