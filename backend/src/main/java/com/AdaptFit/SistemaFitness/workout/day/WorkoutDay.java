package com.AdaptFit.SistemaFitness.workout.day;

import com.AdaptFit.SistemaFitness.workout.exercise.Exercise;
import com.AdaptFit.SistemaFitness.workout.Workout;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "workout_days")
public class WorkoutDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @Column(nullable = false)
    private String name; // e.g., "Monday", "Day 1"

    @Column(name = "day_of_week")
    private Integer dayOfWeek; // 1=Monday, 7=Sunday, optional

    @OneToMany(mappedBy = "workoutDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises;

    // Order of the day in the workout
    @Column(name = "day_order")
    private Integer dayOrder;
}