package com.AdaptFit.SistemaFitness.workout.session;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutSessionRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutSessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workout-sessions")
@RequiredArgsConstructor
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> createWorkoutSession(@RequestBody CreateWorkoutSessionRequest request) {
        System.out.println(">>> WorkoutSessionController.createWorkoutSession() called");
        System.out.println(">>> Request: " + request);

        WorkoutSessionResponse response = workoutSessionService.createWorkoutSession(request);
        System.out.println(">>> WorkoutSession created successfully");
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkoutSessionResponse>>> getWorkoutSessions() {
        List<WorkoutSessionResponse> responses = workoutSessionService.getWorkoutSessionsForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> getWorkoutSession(@PathVariable Long id) {
        WorkoutSessionResponse response = workoutSessionService.getWorkoutSessionById(id);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/day/{workoutDayId}")
    public ResponseEntity<ApiResponse<List<WorkoutSessionResponse>>> getSessionsByWorkoutDay(@PathVariable Long workoutDayId) {
        List<WorkoutSessionResponse> responses = workoutSessionService.getWorkoutSessionsByWorkoutId(workoutDayId);
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> getLatestSessionForUser() {
        WorkoutSessionResponse response = workoutSessionService.getLatestSessionForUser();
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSessionStats() {
        Map<String, Object> stats = workoutSessionService.getSessionStatsForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(stats));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutSession(@PathVariable Long id) {
        workoutSessionService.deleteWorkoutSession(id);
        return ResponseEntity.ok(new ApiResponse<>((Void) null));
    }
}
