package com.AdaptFit.SistemaFitness.ia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {
    @NotBlank(message = "A pergunta não pode estar vazia")
    private String question;
}
