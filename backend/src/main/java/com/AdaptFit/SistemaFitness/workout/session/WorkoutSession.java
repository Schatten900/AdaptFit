package com.AdaptFit.SistemaFitness.workout.session;

import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.workout.Workout;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "workout_sessions")
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @Column
    private Integer durationMinutes; // total duration

    @Column(length = 1000)
    private String notes;
}