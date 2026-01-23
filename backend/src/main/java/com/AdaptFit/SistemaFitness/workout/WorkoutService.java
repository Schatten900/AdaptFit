package com.AdaptFit.SistemaFitness.workout;

import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutResponse;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDay;
import com.AdaptFit.SistemaFitness.workout.exercise.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserService userService;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Transactional
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public WorkoutResponse createWorkout(CreateWorkoutRequest request) {
        validateCreateWorkoutRequest(request);
        User user = getCurrentUser();
        Workout workout = mapToWorkoutEntity(request, user);
        mapWorkoutDays(request.getWorkoutDays(), workout);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("workoutSave");
        Workout saved = circuitBreaker.run(() -> workoutRepository.save(workout),
                this::handleCircuitBreakerFallback);

        return mapToResponse(saved);
    }

    @Retryable(value = {Exception.class}, maxAttempts = 2, backoff = @Backoff(delay = 500))
    public List<WorkoutResponse> getWorkoutsForCurrentUser() {
        User user = getCurrentUser();
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("workoutFind");
        List<Workout> workouts = circuitBreaker.run(() -> workoutRepository.findByUserId(user.getId()),
                this::handleCircuitBreakerFallback);
        return workouts.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Retryable(value = {Exception.class}, maxAttempts = 2, backoff = @Backoff(delay = 500))
    public WorkoutResponse getWorkoutById(Long id) {
        Workout workout = findWorkoutById(id);
        validateWorkoutOwnership(workout);
        return mapToResponse(workout);
    }

    private void validateCreateWorkoutRequest(CreateWorkoutRequest request) {
        if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Workout name is required");
        }
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private Workout mapToWorkoutEntity(CreateWorkoutRequest request, User user) {
        Workout workout = new Workout();
        workout.setName(request.getName().trim());
        workout.setDescription(request.getDescription());
        workout.setUser(user);
        return workout;
    }

    private void mapWorkoutDays(List<com.AdaptFit.SistemaFitness.workout.dto.WorkoutDayRequest> dayRequests, Workout workout) {
        if (dayRequests != null) {
            workout.setWorkoutDays(dayRequests.stream().map(dayReq -> {
                WorkoutDay day = new WorkoutDay();
                day.setName(dayReq.getName());
                day.setDayOfWeek(dayReq.getDayOfWeek());
                day.setDayOrder(dayReq.getDayOrder());
                day.setWorkout(workout);
                mapExercises(dayReq.getExercises(), day);
                return day;
            }).collect(Collectors.toList()));
        }
    }

    private void mapExercises(List<com.AdaptFit.SistemaFitness.workout.dto.ExerciseRequest> exerciseRequests, WorkoutDay day) {
        if (exerciseRequests != null) {
            day.setExercises(exerciseRequests.stream().map(exReq -> {
                Exercise ex = new Exercise();
                ex.setName(exReq.getName());
                ex.setDescription(exReq.getDescription());
                ex.setSets(exReq.getSets());
                ex.setReps(exReq.getReps());
                ex.setWeight(exReq.getWeight());
                ex.setRestTimeSeconds(exReq.getRestTimeSeconds());
                ex.setExerciseOrder(exReq.getExerciseOrder());
                ex.setWorkoutDay(day);
                return ex;
            }).collect(Collectors.toList()));
        }
    }

    private Workout findWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout with id " + id + " not found"));
    }

    private void validateWorkoutOwnership(Workout workout) {
        User user = getCurrentUser();
        if (!workout.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied to workout");
        }
    }

    private WorkoutResponse mapToResponse(Workout workout) {
        WorkoutResponse response = new WorkoutResponse();
        response.setId(workout.getId());
        response.setName(workout.getName());
        response.setDescription(workout.getDescription());
        response.setCreatedAt(workout.getCreatedAt());
        return response;
    }

    private <T> T handleCircuitBreakerFallback(Throwable throwable) {
        throw new RuntimeException("Service temporarily unavailable", throwable);
    }

    @Recover
    public WorkoutResponse recoverCreateWorkout(Exception e, CreateWorkoutRequest request) {
        System.err.println("Failed to create workout after retries: " + e.getMessage());
        return null;
    }
}