package com.AdaptFit.SistemaFitness.feedback;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "workout_session_id")
    private Long workoutSessionId;

    @Column(name = "fatigue_level", nullable = false)
    private Integer fatigueLevel;

    @Column(name = "muscle_soreness", nullable = false)
    private Integer muscleSoreness;

    @Column(length = 1000)
    private String notes;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
