package com.AdaptFit.SistemaFitness.workout.day;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutDayRequest;
import com.AdaptFit.SistemaFitness.workout.dto.UpdateWorkoutDayRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutDayResponse;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutExerciseRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutExerciseResponse;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercise;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercisesRepository;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final ExerciseCatalogRepository exerciseCatalogRepository;
    private final WorkoutExercisesRepository workoutExercisesRepository;
    private final UserService userService;

    @Transactional
    public WorkoutDayResponse createWorkoutDay(CreateWorkoutDayRequest request) {
        validateCreateWorkoutDayRequest(request);

        User user = userService.getCurrentUser();
        WorkoutDay workoutDay = new WorkoutDay();
        workoutDay.setName(request.getName().trim());
        workoutDay.setDescription(request.getDescription());
        workoutDay.setUserId(user.getId());

        WorkoutDay saved = workoutDayRepository.save(workoutDay);

        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            createExercises(request.getExercises(), saved.getId());
        }

        return mapToResponse(saved);
    }

    @Transactional
    public WorkoutDayResponse updateWorkoutDay(Long id, UpdateWorkoutDayRequest request) {
        validateUpdateWorkoutDayRequest(request);

        User user = userService.getCurrentUser();

        WorkoutDay workoutDay = workoutDayRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        if (request.getName() != null && !request.getName().isBlank()) {
            workoutDay.setName(request.getName().trim());
        }
        if (request.getDescription() != null) {
            workoutDay.setDescription(request.getDescription());
        }

        WorkoutDay saved = workoutDayRepository.save(workoutDay);

        if (request.getExercises() != null) {
            workoutExercisesRepository.deleteByWorkoutDayId(saved.getId());
            if (!request.getExercises().isEmpty()) {
                createExercises(request.getExercises(), saved.getId());
            }
        }

        return mapToResponse(saved);
    }

    @Transactional
    public void deleteWorkoutDay(Long id) {
        User user = userService.getCurrentUser();

        WorkoutDay workoutDay = workoutDayRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        workoutDayRepository.delete(workoutDay);
    }

    public List<WorkoutDayResponse> getWorkoutDaysForCurrentUser() {
        User user = userService.getCurrentUser();

        List<WorkoutDay> workoutDays = workoutDayRepository.findByUserId(user.getId());

        return workoutDays.stream().map(this::mapToResponse).toList();
    }

    public List<WorkoutDayResponse> getAvailableWorkoutDays() {
        User user = userService.getCurrentUser();

        List<WorkoutDay> workoutDays = workoutDayRepository.findByUserIdAndWorkoutIdIsNull(user.getId());

        return workoutDays.stream().map(this::mapToResponse).toList();
    }

    private void createExercises(List<WorkoutExerciseRequest> exercises, Long workoutDayId) {
        for (int i = 0; i < exercises.size(); i++) {
            WorkoutExerciseRequest req = exercises.get(i);

            if (req.getExerciseId() == null) {
                throw new ValidationException("Exercicio é obrigatório");
            }

            ExerciseCatalog catalog = exerciseCatalogRepository.findById(req.getExerciseId())
                    .orElseThrow(() ->
                            new NotFoundException("Exercicio não encontrado no sistema")
                    );

            WorkoutExercise ex = new WorkoutExercise();
            ex.setExerciseId(catalog.getId());
            ex.setWorkoutDayId(workoutDayId);
            ex.setSets(req.getSets());
            ex.setReps(req.getReps());
            ex.setWeight(req.getWeight());
            ex.setRestTimeSeconds(req.getRestTimeSeconds());
            ex.setExerciseOrder(i);

            workoutExercisesRepository.save(ex);
        }
    }

    private WorkoutDayResponse mapToResponse(WorkoutDay workoutDay) {
        WorkoutDayResponse response = new WorkoutDayResponse();
        response.setId(workoutDay.getId());
        response.setName(workoutDay.getName());
        response.setDescription(workoutDay.getDescription());
        response.setDayOfWeek(workoutDay.getDayOfWeek());
        response.setDayOrder(workoutDay.getDayOrder());
        response.setCreatedAt(workoutDay.getCreatedAt());
        response.setUpdatedAt(workoutDay.getUpdatedAt());

        List<WorkoutExercise> exercises = workoutExercisesRepository.findByWorkoutDayId(workoutDay.getId());
        if (exercises != null && !exercises.isEmpty()) {
            response.setExercises(
                    exercises.stream()
                            .sorted(Comparator.comparing(WorkoutExercise::getExerciseOrder))
                            .map(this::mapExerciseToResponse)
                            .toList()
            );
        }

        return response;
    }

    private WorkoutExerciseResponse mapExerciseToResponse(WorkoutExercise ex) {
        WorkoutExerciseResponse e = new WorkoutExerciseResponse();
        e.setId(ex.getId());
        e.setExerciseId(ex.getExerciseId());
        e.setSets(ex.getSets());
        e.setReps(ex.getReps());
        e.setWeight(ex.getWeight());
        e.setRestTimeSeconds(ex.getRestTimeSeconds());
        e.setExerciseOrder(ex.getExerciseOrder());
        return e;
    }

    private void validateCreateWorkoutDayRequest(CreateWorkoutDayRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Nome do treino invalido");
        }
    }

    private void validateUpdateWorkoutDayRequest(UpdateWorkoutDayRequest request) {
        if (request == null) {
            throw new ValidationException("Atualização não pode ser vazia");
        }
    }

    public WorkoutDayResponse getWorkoutDayById(Long id) {
        User user = userService.getCurrentUser();

        WorkoutDay workoutDay = workoutDayRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        return mapToResponse(workoutDay);
    }
}
