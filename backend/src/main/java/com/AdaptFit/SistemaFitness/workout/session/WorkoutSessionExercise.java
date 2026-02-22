package com.AdaptFit.SistemaFitness.workout.session;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "workout_session_exercises")
public class WorkoutSessionExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "exercise_id", nullable = false)
    private Long exerciseId;

    @Column(name = "workout_day_id", nullable = false)
    private Long workoutDayId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column
    private Integer reps;

    @Column
    private Double weight;

    @Column
    private Double volume;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
