package com.AdaptFit.SistemaFitness.workout.session;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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

    @Column(name = "workout_day_id", nullable = false)
    private Long workoutDayId;

    @Column(name = "session_date", nullable = false)
    private Date sessionDate;

    @Column(name = "local_date")
    private LocalDate localDate;

    @Column
    private Integer durationMinutes;

    @Column(length = 1000)
    private String notes;

    @Column(name = "total_reps")
    private Integer totalReps;

    @Column(name = "total_weight")
    private Double totalWeight;

    @Column(name = "total_volume")
    private Double totalVolume;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        if (localDate == null) {
            localDate = LocalDate.now();
        }
    }
}
