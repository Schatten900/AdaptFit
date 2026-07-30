package com.AdaptFit.SistemaFitness.enums;

public enum WorkoutSplit {
    FULL_BODY(3, "Full Body", "Treino completo 3x por semana"),
    UPPER_LOWER(4, "Upper/Lower", "Treino superiores e inferiores 4x por semana"),
    PPL(6, "Push/Pull/Legs", "Peito/Costas/Pernas 6x por semana"),
    BRO_SPLIT(5, "Bro Split", "Grupo muscular por dia 5x por semana");

    private final int daysPerWeek;
    private final String label;
    private final String description;

    WorkoutSplit(int daysPerWeek, String label, String description) {
        this.daysPerWeek = daysPerWeek;
        this.label = label;
        this.description = description;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public static WorkoutSplit getForExperienceLevel(ExperienceLevel level, int availableDays) {
        return switch (level) {
            case BEGINNER -> FULL_BODY;
            case INTERMEDIATE -> {
                if (availableDays >= 4) {
                    yield UPPER_LOWER;
                }
                yield FULL_BODY;
            }
            case ADVANCED -> {
                if (availableDays >= 6) {
                    yield PPL;
                } else if (availableDays >= 5) {
                    yield BRO_SPLIT;
                } else if (availableDays >= 4) {
                    yield UPPER_LOWER;
                }
                yield FULL_BODY;
            }
        };
    }
}
