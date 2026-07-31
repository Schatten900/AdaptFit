package com.AdaptFit.SistemaFitness.ia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponse {
    private String content;
    private boolean success;
    private String errorMessage;
}
