package com.AdaptFit.SistemaFitness.feedback;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.feedback.dto.CreateFeedbackRequest;
import com.AdaptFit.SistemaFitness.feedback.dto.FeedbackResponse;
import com.AdaptFit.SistemaFitness.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<FeedbackResponse>> createFeedback(
            @Valid @RequestBody CreateFeedbackRequest request) {
        Long userId = userService.getCurrentUserId();
        FeedbackResponse response = feedbackService.createFeedback(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getFeedbacks() {
        Long userId = userService.getCurrentUserId();
        List<FeedbackResponse> responses = feedbackService.getFeedbacksForUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedback(@PathVariable Long id) {
        Long userId = userService.getCurrentUserId();
        FeedbackResponse response = feedbackService.getFeedbackById(id, userId);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/session/{workoutSessionId}")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getFeedbacksBySession(
            @PathVariable Long workoutSessionId) {
        Long userId = userService.getCurrentUserId();
        List<FeedbackResponse> responses = feedbackService.getFeedbacksByWorkoutSession(workoutSessionId, userId);
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        Long userId = userService.getCurrentUserId();
        feedbackService.deleteFeedback(id, userId);
        return ResponseEntity.ok(new ApiResponse<>((Void) null));
    }
}
