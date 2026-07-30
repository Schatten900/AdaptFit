package com.AdaptFit.SistemaFitness.enums;

public enum AdjustmentReason {
    INITIAL_PLAN("Plano inicial criado"),
    PROGRESS_SUCCESS("Progresso bem-sucedido - aumentando carga"),
    PROGRESS_STAGNATION("Estagnação - mudando estímulo"),
    REGRESSION("Retrocesso - reduzindo carga"),
    WEIGHT_LOSS_CUTTING("Perda de peso insuficiente - reduzindo calorias"),
    WEIGHT_GAIN_BULKING("Ganho de peso insuficiente - aumentando calorias"),
    HIGH_FATIGUE("Fadiga elevada detectada"),
    OVERTRAINING("Overtraining detectado - deload"),
    RECOVERY("Período de recuperação"),
    MANUAL_ADJUSTMENT("Ajuste manual");

    private final String description;

    AdjustmentReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
