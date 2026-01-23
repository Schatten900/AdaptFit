package com.AdaptFit.SistemaFitness.workout;

import com.AdaptFit.SistemaFitness.common.ApiResponse;
 
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutRequest;
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutSessionRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutResponse;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutSessionResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutSessionService workoutSessionService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkoutResponse>> createWorkout(@RequestBody CreateWorkoutRequest request) {
        WorkoutResponse response = workoutService.createWorkout(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkoutResponse>>> getWorkouts() {
        List<WorkoutResponse> responses = workoutService.getWorkoutsForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutResponse>> getWorkout(@PathVariable Long id) {
        WorkoutResponse response = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @PostMapping("/sessions")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> createWorkoutSession(@RequestBody CreateWorkoutSessionRequest request) {
        WorkoutSessionResponse response = workoutSessionService.createWorkoutSession(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<WorkoutSessionResponse>>> getWorkoutHistory() {
        List<WorkoutSessionResponse> responses = workoutSessionService.getWorkoutHistoryForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ApiResponse<Void>(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(new ApiResponse<Void>("Internal server error: " + e.getMessage()));
    }
}
