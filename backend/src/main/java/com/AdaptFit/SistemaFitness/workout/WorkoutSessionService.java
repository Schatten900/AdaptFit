package com.AdaptFit.SistemaFitness.workout;

import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutSessionRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutSessionResponse;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSession;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRepository workoutRepository;
    private final UserService userService;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public WorkoutSessionResponse createWorkoutSession(CreateWorkoutSessionRequest request) {
        validateCreateWorkoutSessionRequest(request);
        User user = getCurrentUser();
        Workout workout = findWorkoutById(request.getWorkoutId());
        WorkoutSession session = mapToWorkoutSessionEntity(request, user, workout);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("sessionSave");
        WorkoutSession saved = circuitBreaker.run(() -> workoutSessionRepository.save(session),
                this::handleCircuitBreakerFallback);

        return mapToResponse(saved);
    }

    @Retryable(value = {Exception.class}, maxAttempts = 2, backoff = @Backoff(delay = 500))
    public List<WorkoutSessionResponse> getWorkoutHistoryForCurrentUser() {
        User user = getCurrentUser();
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("sessionFind");
        List<WorkoutSession> sessions = circuitBreaker.run(() -> workoutSessionRepository.findByUserIdOrderBySessionDateDesc(user.getId()),
                this::handleCircuitBreakerFallback);
        return sessions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private void validateCreateWorkoutSessionRequest(CreateWorkoutSessionRequest request) {
        if (request == null || request.getWorkoutId() == null || request.getSessionDate() == null) {
            throw new IllegalArgumentException("Workout ID and session date are required");
        }
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private Workout findWorkoutById(Long workoutId) {
        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout with id " + workoutId + " not found"));
    }

    private WorkoutSession mapToWorkoutSessionEntity(CreateWorkoutSessionRequest request, User user, Workout workout) {
        WorkoutSession session = new WorkoutSession();
        session.setUser(user);
        session.setWorkout(workout);
        session.setSessionDate(request.getSessionDate());
        session.setDurationMinutes(request.getDurationMinutes());
        session.setNotes(request.getNotes());
        return session;
    }

    private WorkoutSessionResponse mapToResponse(WorkoutSession session) {
        WorkoutSessionResponse response = new WorkoutSessionResponse();
        response.setId(session.getId());
        response.setWorkoutId(session.getWorkout().getId());
        response.setWorkoutName(session.getWorkout().getName());
        response.setSessionDate(session.getSessionDate());
        response.setDurationMinutes(session.getDurationMinutes());
        response.setNotes(session.getNotes());
        return response;
    }

    private <T> T handleCircuitBreakerFallback(Throwable throwable) {
        throw new RuntimeException("Service temporarily unavailable", throwable);
    }

    @Recover
    public WorkoutSessionResponse recoverCreateWorkoutSession(Exception e, CreateWorkoutSessionRequest request) {
        System.err.println("Failed to create workout session after retries: " + e.getMessage());
        return null;
    }
}
