package com.AdaptFit.SistemaFitness.ia;

import lombok.Data;

@Data
public class LlmResponse {
    private String content;
    private String model;
    private Double temperature;
    private long latencyMs;
    private int tokensIn;
    private int tokensOut;
    private String fullPrompt;
    private boolean success;
    private String errorMessage;
}
