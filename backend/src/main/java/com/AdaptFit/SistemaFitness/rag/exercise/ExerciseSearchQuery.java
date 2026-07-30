package com.AdaptFit.SistemaFitness.rag.exercise;

import com.AdaptFit.SistemaFitness.enums.Equipment;
import com.AdaptFit.SistemaFitness.enums.ExerciseLevel;
import com.AdaptFit.SistemaFitness.enums.ExerciseType;
import lombok.Data;

@Data
public class ExerciseSearchQuery {
    private String keyword;
    private String muscleGroup;
    private ExerciseLevel level;
    private Equipment equipment;
    private ExerciseType type;
    private Integer limit;
}
