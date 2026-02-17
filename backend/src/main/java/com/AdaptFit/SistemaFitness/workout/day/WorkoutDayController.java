package com.AdaptFit.SistemaFitness.workout.day;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutDayRequest;
import com.AdaptFit.SistemaFitness.workout.dto.UpdateWorkoutDayRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutDayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-days")
@RequiredArgsConstructor
public class WorkoutDayController {

    private final WorkoutDayService workoutDayService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkoutDayResponse>> createWorkoutDay(
            @RequestBody CreateWorkoutDayRequest request) {

        WorkoutDayResponse response = workoutDayService.createWorkoutDay(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkoutDayResponse>>> getWorkoutDays() {

        List<WorkoutDayResponse> responses =
                workoutDayService.getWorkoutDaysForCurrentUser();

        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<WorkoutDayResponse>>> getAvailableWorkoutDays() {

        List<WorkoutDayResponse> responses =
                workoutDayService.getAvailableWorkoutDays();

        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutDayResponse>> getWorkoutDay(
            @PathVariable Long id) {

        WorkoutDayResponse response =
                workoutDayService.getWorkoutDayById(id);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutDayResponse>> updateWorkoutDay(
            @PathVariable Long id,
            @RequestBody UpdateWorkoutDayRequest request) {

        WorkoutDayResponse response =
                workoutDayService.updateWorkoutDay(id, request);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutDay(
            @PathVariable Long id) {

        workoutDayService.deleteWorkoutDay(id);
        return ResponseEntity.ok(new ApiResponse<>((Void) null));
    }
}
