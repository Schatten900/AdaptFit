package com.AdaptFit.SistemaFitness.preferences.dto;

import lombok.Data;

import java.util.List;

@Data
public class PreferencesResponse {
    private Long userId;
    private List<String> availableEquipment;
    private List<String> injuries;
    private List<String> exerciseBlacklist;
}
