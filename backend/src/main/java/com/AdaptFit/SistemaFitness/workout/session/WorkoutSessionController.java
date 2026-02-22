package com.AdaptFit.SistemaFitness.workout.session;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.workout.dto.Evolution.DashboardResponse;
import com.AdaptFit.SistemaFitness.workout.dto.History.HistoricoResponse;
import com.AdaptFit.SistemaFitness.workout.dto.Session.CreateWorkoutSessionRequest;
import com.AdaptFit.SistemaFitness.workout.dto.Session.WorkoutSessionResponse;
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
        WorkoutSessionResponse response = workoutSessionService.createWorkoutSession(request);
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
        List<WorkoutSessionResponse> responses = workoutSessionService.getWorkoutSessionsByWorkoutDayId(workoutDayId);
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> getLatestSessionForUser() {
        WorkoutSessionResponse response = workoutSessionService.getLatestSessionForUser();
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/latest/{workoutDayId}")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> getLatestSessionByWorkoutDayId(@PathVariable Long workoutDayId) {
        WorkoutSessionResponse response = workoutSessionService.getLatestSessionByWorkoutDayId(workoutDayId);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSessionStats() {
        Map<String, Object> stats = workoutSessionService.getSessionStatsForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(stats));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> getHistory(
            @RequestParam(required = false, defaultValue = "month") String period,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) Long exerciseId,
            @RequestParam(required = false) Long workoutId) {
        List<HistoricoResponse> history = workoutSessionService.getHistory(period, muscleGroup, exerciseId, workoutId);
        return ResponseEntity.ok(new ApiResponse<>(history));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @RequestParam(required = false, defaultValue = "month") String period,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) Long exerciseId,
            @RequestParam(required = false) Long workoutId) {
        DashboardResponse dashboard = workoutSessionService.getDashboard(period, muscleGroup, exerciseId, workoutId);
        return ResponseEntity.ok(new ApiResponse<>(dashboard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutSession(@PathVariable Long id) {
        workoutSessionService.deleteWorkoutSession(id);
        return ResponseEntity.ok(new ApiResponse<>((Void) null));
    }
}
