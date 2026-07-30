package com.AdaptFit.SistemaFitness.rag.dto;

import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class ExerciseResponse {
    private Long id;
    private String name;
    private String primaryMuscle;
    private List<String> secondaryMuscles;
    private String description;
    private Boolean isBodyweight;

    public static ExerciseResponse fromEntity(ExerciseCatalog exercise) {
        ExerciseResponse response = new ExerciseResponse();
        response.setId(exercise.getId());
        response.setName(exercise.getName());
        response.setPrimaryMuscle(exercise.getPrimaryMuscle());
        response.setDescription(exercise.getDescription());
        response.setIsBodyweight(exercise.getIsBodyweight());

        if (exercise.getSecondaryMuscles() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<String> list = mapper.readValue(
                    exercise.getSecondaryMuscles(),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class)
                );
                response.setSecondaryMuscles(list);
            } catch (JsonProcessingException e) {
                response.setSecondaryMuscles(Collections.emptyList());
            }
        } else {
            response.setSecondaryMuscles(Collections.emptyList());
        }

        return response;
    }
}
