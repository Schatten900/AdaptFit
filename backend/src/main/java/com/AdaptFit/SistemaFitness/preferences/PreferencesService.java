package com.AdaptFit.SistemaFitness.preferences;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.preferences.dto.PreferencesRequest;
import com.AdaptFit.SistemaFitness.preferences.dto.PreferencesResponse;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreferencesService {

    private final PreferencesRepository preferencesRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public PreferencesResponse getPreferences() {
        Long userId = userService.getCurrentUserId();
        UserPreferences prefs = preferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Preferences not found"));
        return mapToResponse(prefs);
    }

    @Transactional
    public PreferencesResponse createPreferences(PreferencesRequest request) {
        Long userId = userService.getCurrentUserId();

        if (preferencesRepository.findByUserId(userId).isPresent()) {
            return updatePreferences(request);
        }

        UserPreferences prefs = new UserPreferences();
        prefs.setUserId(userId);
        prefs.setAvailableEquipment(toJson(request.getAvailableEquipment()));
        prefs.setInjuries(toJson(request.getInjuries()));
        prefs.setExerciseBlacklist(toJson(request.getExerciseBlacklist()));

        UserPreferences saved = preferencesRepository.save(prefs);
        log.info("Created preferences for user {}", userId);
        return mapToResponse(saved);
    }

    @Transactional
    public PreferencesResponse updatePreferences(PreferencesRequest request) {
        Long userId = userService.getCurrentUserId();
        UserPreferences prefs = preferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Preferences not found"));

        if (request.getAvailableEquipment() != null) {
            prefs.setAvailableEquipment(toJson(request.getAvailableEquipment()));
        }
        if (request.getInjuries() != null) {
            prefs.setInjuries(toJson(request.getInjuries()));
        }
        if (request.getExerciseBlacklist() != null) {
            prefs.setExerciseBlacklist(toJson(request.getExerciseBlacklist()));
        }

        UserPreferences saved = preferencesRepository.save(prefs);
        log.info("Updated preferences for user {}", userId);
        return mapToResponse(saved);
    }

    private PreferencesResponse mapToResponse(UserPreferences entity) {
        PreferencesResponse resp = new PreferencesResponse();
        resp.setUserId(entity.getUserId());
        resp.setAvailableEquipment(fromJson(entity.getAvailableEquipment()));
        resp.setInjuries(fromJson(entity.getInjuries()));
        resp.setExerciseBlacklist(fromJson(entity.getExerciseBlacklist()));
        return resp;
    }

    private String toJson(List<String> list) {
        try {
            return list == null ? "[]" : objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            log.error("Error serializing list to JSON", e);
            return "[]";
        }
    }

    private List<String> fromJson(String json) {
        try {
            return json == null || json.isBlank()
                    ? Collections.emptyList()
                    : objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.error("Error deserializing JSON to list", e);
            return Collections.emptyList();
        }
    }
}
