package com.AdaptFit.SistemaFitness.workout.split;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.workout.dto.CreateSplitRequest;
import com.AdaptFit.SistemaFitness.workout.dto.SplitResponse;
import com.AdaptFit.SistemaFitness.workout.dto.UpdateSplitRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutDayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/splits")
@RequiredArgsConstructor
public class SplitController {

    private final SplitService splitService;

    @PostMapping
    public ResponseEntity<ApiResponse<SplitResponse>> createSplit(@RequestBody CreateSplitRequest request) {
        SplitResponse response = splitService.createSplit(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SplitResponse>>> getSplits() {
        List<SplitResponse> responses = splitService.getSplitsForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SplitResponse>>> getActiveSplits() {
        List<SplitResponse> responses = splitService.getActiveSplitsForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SplitResponse>> getSplit(@PathVariable Long id) {
        SplitResponse response = splitService.getSplitById(id);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SplitResponse>> updateSplit(
            @PathVariable Long id,
            @RequestBody UpdateSplitRequest request) {
        SplitResponse response = splitService.updateSplit(id, request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSplit(@PathVariable Long id) {
        splitService.deleteSplit(id);
        return ResponseEntity.ok(new ApiResponse<Void>((Void) null));
    }

    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<ApiResponse<WorkoutDayResponse>> getWorkoutForDay(@PathVariable Integer dayOfWeek) {
        WorkoutDayResponse response = splitService.getWorkoutForDay(dayOfWeek);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<WorkoutDayResponse>> getWorkoutForToday() {
        WorkoutDayResponse response = splitService.getWorkoutForToday();
        return ResponseEntity.ok(new ApiResponse<>(response));
    }
}
