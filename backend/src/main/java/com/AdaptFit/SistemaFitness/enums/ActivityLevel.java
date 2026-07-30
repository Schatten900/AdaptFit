package com.AdaptFit.SistemaFitness.enums;

public enum ActivityLevel {
    SEDENTARY(1.2, "Sedentário", "Pouca ou nenhuma atividade física"),
    LIGHTLY_ACTIVE(1.375, "Levemente ativo", "Exercício leve 1-3 dias/semana"),
    MODERATELY_ACTIVE(1.55, "Moderadamente ativo", "Exercício moderado 3-5 dias/semana"),
    VERY_ACTIVE(1.725, "Muito ativo", "Exercício intenso 6-7 dias/semana"),
    EXTRA_ACTIVE(1.9, "Extra ativo", "Exercício muito intenso, trabalho físico");

    private final double multiplier;
    private final String label;
    private final String description;

    ActivityLevel(double multiplier, String label, String description) {
        this.multiplier = multiplier;
        this.label = label;
        this.description = description;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
