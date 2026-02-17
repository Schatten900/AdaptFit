package com.AdaptFit.SistemaFitness.feedback.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FeedbackResponse {

    private Long id;
    private Long userId;
    private Long workoutSessionId;
    private Integer fatigueLevel;
    private Integer muscleSoreness;
    private String notes;
    private Date createdAt;
}
