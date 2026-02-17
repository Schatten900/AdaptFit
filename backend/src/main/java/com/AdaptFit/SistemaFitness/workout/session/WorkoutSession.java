package com.AdaptFit.SistemaFitness.workout.session;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "workout_sessions")
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "workout_id", nullable = false)
    private Long workoutId;

    @Column(name = "session_date", nullable = false)
    private Date sessionDate;

    @Column
    private Integer durationMinutes;

    @Column(length = 1000)
    private String notes;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
