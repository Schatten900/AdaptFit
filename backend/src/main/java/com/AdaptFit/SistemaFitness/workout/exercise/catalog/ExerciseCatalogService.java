package com.AdaptFit.SistemaFitness.workout.exercise.catalog;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseCatalogService {

    private final ExerciseCatalogRepository exerciseCatalogRepository;

    public List<ExerciseCatalog> getAllExercises(){
        List<ExerciseCatalog> listExercise = exerciseCatalogRepository.findAll();
        if (listExercise.isEmpty()){
            throw new NotFoundException("Nenhum exercicio encontrado no catalogo");
        }
        return listExercise;
    }

}
