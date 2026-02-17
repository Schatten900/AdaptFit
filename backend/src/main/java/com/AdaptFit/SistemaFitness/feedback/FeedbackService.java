package com.AdaptFit.SistemaFitness.feedback;

import com.AdaptFit.SistemaFitness.common.exception.BusinessException;
import com.AdaptFit.SistemaFitness.feedback.dto.CreateFeedbackRequest;
import com.AdaptFit.SistemaFitness.feedback.dto.FeedbackResponse;
import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserRepository;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSession;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    @Transactional
    public FeedbackResponse createFeedback(CreateFeedbackRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        WorkoutSession session = workoutSessionRepository.findById(request.getWorkoutSessionId())
                .orElseThrow(() -> new BusinessException("Workout session not found"));

        if (!session.getUserId().equals(userId)) {
            throw new BusinessException("Workout session does not belong to user");
        }

        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setWorkoutSessionId(request.getWorkoutSessionId());
        feedback.setFatigueLevel(request.getFatigueLevel());
        feedback.setMuscleSoreness(request.getMuscleSoreness());
        feedback.setNotes(request.getNotes());

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return mapToResponse(savedFeedback);
    }

    public List<FeedbackResponse> getFeedbacksForUser(Long userId) {
        List<Feedback> feedbacks = feedbackRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return feedbacks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FeedbackResponse getFeedbackById(Long id, Long userId) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Feedback not found"));

        if (!feedback.getUserId().equals(userId)) {
            throw new BusinessException("Feedback does not belong to user");
        }

        return mapToResponse(feedback);
    }

    public List<FeedbackResponse> getFeedbacksByWorkoutSession(Long workoutSessionId, Long userId) {
        List<Feedback> feedbacks = feedbackRepository.findByWorkoutSessionIdAndUserId(workoutSessionId, userId);
        return feedbacks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFeedback(Long id, Long userId) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Feedback not found"));

        if (!feedback.getUserId().equals(userId)) {
            throw new BusinessException("Feedback does not belong to user");
        }

        feedbackRepository.delete(feedback);
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setId(feedback.getId());
        response.setUserId(feedback.getUserId());
        response.setWorkoutSessionId(feedback.getWorkoutSessionId());
        response.setFatigueLevel(feedback.getFatigueLevel());
        response.setMuscleSoreness(feedback.getMuscleSoreness());
        response.setNotes(feedback.getNotes());
        response.setCreatedAt(feedback.getCreatedAt());
        return response;
    }
}
