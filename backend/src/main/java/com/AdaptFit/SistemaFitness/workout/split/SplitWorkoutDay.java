package com.AdaptFit.SistemaFitness.workout.split;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "split_workout_days")
public class SplitWorkoutDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "split_id", nullable = false)
    private Long splitId;

    @Column(name = "workout_day_id", nullable = false)
    private Long workoutDayId;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "day_order", nullable = false)
    private Integer dayOrder;
}
