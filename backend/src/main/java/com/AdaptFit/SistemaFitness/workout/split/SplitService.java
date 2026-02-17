package com.AdaptFit.SistemaFitness.workout.split;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDay;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDayRepository;
import com.AdaptFit.SistemaFitness.workout.dto.CreateSplitRequest;
import com.AdaptFit.SistemaFitness.workout.dto.SplitResponse;
import com.AdaptFit.SistemaFitness.workout.dto.UpdateSplitRequest;
import com.AdaptFit.SistemaFitness.workout.dto.SplitWorkoutDayRequest;
import com.AdaptFit.SistemaFitness.workout.dto.SplitWorkoutDayResponse;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutDayResponse;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDayService;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercise;
import com.AdaptFit.SistemaFitness.workout.exercise.WorkoutExercisesRepository;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SplitService {

    private final SplitRepository splitRepository;
    private final SplitWorkoutDayRepository splitWorkoutDayRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutDayService workoutDayService;
    private final UserService userService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final WorkoutExercisesRepository workoutExercisesRepository;
    private final ExerciseCatalogRepository exerciseCatalogRepository;

    @Transactional
    public SplitResponse createSplit(CreateSplitRequest request) {
        validateCreateSplitRequest(request);

        User user = userService.getCurrentUser();
        Split splitToSave = mapToSplitEntity(request, user);

        splitToSave.setUserId(user.getId());

        CircuitBreaker cb = circuitBreakerFactory.create("splitSave");
        Split split = cb.run(() -> splitRepository.save(splitToSave),
                this::handleCircuitBreakerFallback);

        if (request.getSplitWorkoutDays() != null && !request.getSplitWorkoutDays().isEmpty()) {
            final List<SplitWorkoutDayRequest> splitWorkoutDays = request.getSplitWorkoutDays();
            final Long splitId = split.getId();
            CircuitBreaker cbSplitWorkout = circuitBreakerFactory.create("splitWorkoutDaySave");
            cbSplitWorkout.run(() -> {
                mapSplitWorkoutDays(splitWorkoutDays, splitId);
                return null;
            }, this::handleCircuitBreakerFallback);
        }

        return mapToResponse(split);
    }

    @Transactional
    public SplitResponse updateSplit(Long id, UpdateSplitRequest request) {
        validateUpdateSplitRequest(request);

        User user = userService.getCurrentUser();

        CircuitBreaker cb = circuitBreakerFactory.create("splitFind");
        Split split = cb.run(() -> splitRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Divisão de treino não encontrada")),
                this::handleCircuitBreakerFallback);

        updateSplitFromRequest(split, request);

        if (request.getSplitWorkoutDays() != null) {
            final Long splitId = split.getId();
            CircuitBreaker cbDelete = circuitBreakerFactory.create("splitWorkoutDayDelete");
            cbDelete.run(() -> {
                splitWorkoutDayRepository.deleteBySplitId(splitId);
                return null;
            }, this::handleCircuitBreakerFallback);

            if (!request.getSplitWorkoutDays().isEmpty()) {
                final List<SplitWorkoutDayRequest> splitWorkoutDays = request.getSplitWorkoutDays();
                CircuitBreaker cbCreate = circuitBreakerFactory.create("splitWorkoutDaySave");
                cbCreate.run(() -> {
                    mapSplitWorkoutDays(splitWorkoutDays, splitId);
                    return null;
                }, this::handleCircuitBreakerFallback);
            }
        }

        split = splitRepository.save(split);
        return mapToResponse(split);
    }

    @Transactional
    public void deleteSplit(Long id) {
        User user = userService.getCurrentUser();

        CircuitBreaker cb = circuitBreakerFactory.create("splitFind");
        Split split = cb.run(() -> splitRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Divisão de treino não encontrada")),
                this::handleCircuitBreakerFallback);

        CircuitBreaker cbDelete = circuitBreakerFactory.create("splitDelete");
        cbDelete.run(() -> {
            splitRepository.delete(split);
            return null;
        }, this::handleCircuitBreakerFallback);
    }

    public List<SplitResponse> getSplitsForCurrentUser() {
        User user = userService.getCurrentUser();

        CircuitBreaker cb = circuitBreakerFactory.create("splitFind");
        List<Split> splits = cb.run(() -> splitRepository.findByUserId(user.getId()),
                this::handleCircuitBreakerFallback);

        return splits.stream().map(this::mapToResponse).toList();
    }

    public List<SplitResponse> getActiveSplitsForCurrentUser() {
        User user = userService.getCurrentUser();

        CircuitBreaker cb = circuitBreakerFactory.create("splitFind");
        List<Split> splits = cb.run(() -> splitRepository.findByUserIdAndActiveTrue(user.getId()),
                this::handleCircuitBreakerFallback);

        return splits.stream().map(this::mapToResponse).toList();
    }

    public SplitResponse getSplitById(Long id) {
        User user = userService.getCurrentUser();

        CircuitBreaker cb = circuitBreakerFactory.create("splitFind");
        Split split = cb.run(() -> splitRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Divisão de treino não encontrada")),
                this::handleCircuitBreakerFallback);

        return mapToResponse(split);
    }

    public WorkoutDayResponse getWorkoutForDay(Integer dayOfWeek) {
        validateDayOfWeek(dayOfWeek);

        User user = userService.getCurrentUser();

        CircuitBreaker cb = circuitBreakerFactory.create("splitFind");
        List<Split> splits = cb.run(() -> splitRepository.findByUserIdAndActiveTrue(user.getId()),
                this::handleCircuitBreakerFallback);

        if (splits.isEmpty()) {
            throw new ValidationException("Nenhuma divisão de treino ativa para este usuario");
        }

        Split activeSplit = splits.get(0);

        CircuitBreaker cbSplitWorkout = circuitBreakerFactory.create("splitWorkoutDayFind");
        List<SplitWorkoutDay> splitWorkoutDays = cbSplitWorkout.run(
                () -> splitWorkoutDayRepository.findBySplitId(activeSplit.getId()),
                this::handleCircuitBreakerFallback
        );

        SplitWorkoutDay found = splitWorkoutDays.stream()
                .filter(swd -> swd.getDayOfWeek().equals(dayOfWeek))
                .findFirst()
                .orElse(null);

        if (found == null) {
            throw new ValidationException("Nenhum treino configurado para: " + dayOfWeek);
        }

        return workoutDayService.getWorkoutDayById(found.getWorkoutDayId());
    }

    public WorkoutDayResponse getWorkoutForToday() {
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        CircuitBreaker cb = circuitBreakerFactory.create("splitFind");
        return cb.run(() -> getWorkoutForDay(dayOfWeek),
                this::handleCircuitBreakerFallback);
    }

    private Split mapToSplitEntity(CreateSplitRequest request, User user) {
        Split split = new Split();
        split.setName(request.getName().trim());
        split.setDescription(request.getDescription());
        split.setUserId(user.getId());
        split.setActive(true);
        return split;
    }

    private void updateSplitFromRequest(Split split, UpdateSplitRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            split.setName(request.getName().trim());
        }
        if (request.getDescription() != null) {
            split.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            split.setActive(request.getActive());
        }
    }

    private void mapSplitWorkoutDays(List<SplitWorkoutDayRequest> requests, Long splitId) {
        for (SplitWorkoutDayRequest req : requests) {
            if (req.getWorkoutDayId() == null) {
                throw new ValidationException("Treino é obrigatório");
            }

            validateDayOfWeek(req.getDayOfWeek());
            validateDayOrder(req.getDayOrder());

            SplitWorkoutDay splitWorkoutDay = new SplitWorkoutDay();
            splitWorkoutDay.setSplitId(splitId);
            splitWorkoutDay.setWorkoutDayId(req.getWorkoutDayId());
            splitWorkoutDay.setDayOfWeek(req.getDayOfWeek());
            splitWorkoutDay.setDayOrder(req.getDayOrder());

            splitWorkoutDayRepository.save(splitWorkoutDay);
        }
    }

    private SplitResponse mapToResponse(Split split) {
        SplitResponse response = new SplitResponse();
        response.setId(split.getId());
        response.setName(split.getName());
        response.setDescription(split.getDescription());
        response.setActive(split.getActive());
        response.setCreatedAt(split.getCreatedAt());
        response.setUpdatedAt(split.getUpdatedAt());

        List<SplitWorkoutDay> splitWorkoutDays = splitWorkoutDayRepository.findBySplitId(split.getId());

        response.setSplitWorkoutDays(
                splitWorkoutDays.stream()
                        .sorted(Comparator.comparing(SplitWorkoutDay::getDayOrder))
                        .map(swd -> {
                            WorkoutDay workoutDay = workoutDayRepository.findById(swd.getWorkoutDayId()).orElse(null);

                            SplitWorkoutDayResponse d = new SplitWorkoutDayResponse();
                            d.setId(swd.getId());
                            d.setSplitId(swd.getSplitId());
                            d.setWorkoutDayId(swd.getWorkoutDayId());
                            d.setWorkoutDayName(workoutDay != null ? workoutDay.getName() : "Unknown");
                            d.setDayOfWeek(swd.getDayOfWeek());
                            d.setDayOrder(swd.getDayOrder());
                            return d;
                        }).toList()
        );

        return response;
    }

    private void validateCreateSplitRequest(CreateSplitRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Nome para a divisão de treino é obrigatória");
        }

        if (request.getSplitWorkoutDays() == null || request.getSplitWorkoutDays().isEmpty()) {
            throw new ValidationException("Divisão de treino deve ter ao menos um treino");
        }
    }

    private void validateUpdateSplitRequest(UpdateSplitRequest request) {
        if (request == null) {
            throw new ValidationException("Atualização não pode ser vazia");
        }
    }

    private void validateDayOfWeek(Integer dayOfWeek) {
        if (dayOfWeek == null || dayOfWeek < 1 || dayOfWeek > 7) {
            throw new ValidationException("Dias da semana devem ser informados");
        }
    }

    private void validateDayOrder(Integer dayOrder) {
        if (dayOrder == null || dayOrder < 0) {
            throw new ValidationException("Ordem de treino invalida");
        }
    }

    private <T> T handleCircuitBreakerFallback(Throwable t) {
        if (t instanceof RuntimeException re) {
            throw re;
        }
        throw new RuntimeException("Serviço temporariamente indisponível", t);
    }

}
