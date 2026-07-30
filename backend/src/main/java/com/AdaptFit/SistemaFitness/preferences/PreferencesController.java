package com.AdaptFit.SistemaFitness.preferences;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.preferences.dto.PreferencesRequest;
import com.AdaptFit.SistemaFitness.preferences.dto.PreferencesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferencesController {

    private final PreferencesService preferencesService;

    @GetMapping
    public ResponseEntity<ApiResponse<PreferencesResponse>> getPreferences() {
        try {
            PreferencesResponse resp = preferencesService.getPreferences();
            return ResponseEntity.ok(new ApiResponse<>(resp));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PreferencesResponse>> createPreferences(
            @Valid @RequestBody PreferencesRequest request) {
        PreferencesResponse resp = preferencesService.createPreferences(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(resp));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PreferencesResponse>> updatePreferences(
            @Valid @RequestBody PreferencesRequest request) {
        try {
            PreferencesResponse resp = preferencesService.updatePreferences(request);
            return ResponseEntity.ok(new ApiResponse<>(resp));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
