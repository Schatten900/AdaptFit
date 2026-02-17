package com.AdaptFit.SistemaFitness.workout.session;

import com.AdaptFit.SistemaFitness.common.exception.AuthenticationException;
import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDay;
import com.AdaptFit.SistemaFitness.workout.day.WorkoutDayRepository;
import com.AdaptFit.SistemaFitness.workout.dto.CreateWorkoutSessionRequest;
import com.AdaptFit.SistemaFitness.workout.dto.WorkoutSessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final UserService userService;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Transactional
    public WorkoutSessionResponse createWorkoutSession(CreateWorkoutSessionRequest request) {
        validateCreateWorkoutSessionRequest(request);
        User user = getCurrentUser();
        validateWorkoutExists(request.getWorkoutId(), user.getId());
        WorkoutSession session = mapToWorkoutSessionEntity(request, user.getId(), request.getWorkoutId());

        WorkoutSession saved = workoutSessionRepository.save(session);

        return mapToResponse(saved);
    }

    public List<WorkoutSessionResponse> getWorkoutSessionsForCurrentUser() {
        User user = getCurrentUser();
        List<WorkoutSession> sessions = workoutSessionRepository.findByUserIdOrderBySessionDateDesc(user.getId());
        return sessions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<WorkoutSessionResponse> getWorkoutSessionsByWorkoutId(Long workoutId) {
        User user = getCurrentUser();
        validateWorkoutExists(workoutId, user.getId());
        List<WorkoutSession> sessions = workoutSessionRepository.findByWorkoutIdOrderBySessionDateDesc(workoutId);
        return sessions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public WorkoutSessionResponse getWorkoutSessionById(Long id) {
        User user = getCurrentUser();
        WorkoutSession session = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão não encontrada"));

        if (!session.getUserId().equals(user.getId())) {
            throw new AuthenticationException("Sessão não pertence ao usuario");
        }

        return mapToResponse(session);
    }

    public WorkoutSessionResponse getLatestSessionForUser() {
        User user = getCurrentUser();
        Optional<WorkoutSession> session = workoutSessionRepository.findFirstByUserIdOrderBySessionDateDesc(user.getId());

        return session.map(this::mapToResponse).orElse(null);
    }

    public Map<String, Object> getSessionStatsForCurrentUser() {
        User user = getCurrentUser();
        return getSessionStats(user.getId());
    }

    public Map<String, Object> getSessionStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", workoutSessionRepository.countByUserId(userId));
        stats.put("totalDurationMinutes", workoutSessionRepository.sumDurationByUserId(userId));
        stats.put("lastSessionDate", workoutSessionRepository.findLastSessionDateByUserId(userId));
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date weekStart = cal.getTime();
        stats.put("daysTrainedThisWeek", workoutSessionRepository.countSessionsInWeek(userId, weekStart));
        
        return stats;
    }

    @Transactional
    public void deleteWorkoutSession(Long id) {
        User user = getCurrentUser();

        WorkoutSession session = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão não encontrada"));

        if (!session.getUserId().equals(user.getId())) {
            throw new AuthenticationException("Sessão não pertence ao usuario");
        }

        workoutSessionRepository.deleteById(id);
    }

    private void validateCreateWorkoutSessionRequest(CreateWorkoutSessionRequest request) {
        if (request == null || request.getWorkoutId() == null || request.getSessionDate() == null) {
            throw new ValidationException("Treino invalido");
        }
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private void validateWorkoutExists(Long workoutId, Long userId) {
        WorkoutDay workoutDay = workoutDayRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        if (!workoutDay.getUserId().equals(userId)) {
            throw new AuthenticationException("Sessão não pertence ao usuario");
        }
    }

    private WorkoutSession mapToWorkoutSessionEntity(CreateWorkoutSessionRequest request, Long userId, Long workoutId) {
        WorkoutSession session = new WorkoutSession();
        session.setUserId(userId);
        session.setWorkoutId(workoutId);
        if (request.getSessionDate() != null) {
            session.setSessionDate(java.sql.Timestamp.valueOf(request.getSessionDate()));
        }
        session.setDurationMinutes(request.getDurationMinutes());
        session.setNotes(request.getNotes());
        return session;
    }

    private WorkoutSessionResponse mapToResponse(WorkoutSession session) {
        WorkoutSessionResponse response = new WorkoutSessionResponse();
        response.setId(session.getId());
        response.setWorkoutId(session.getWorkoutId());
        response.setSessionDate(session.getSessionDate());
        response.setDurationMinutes(session.getDurationMinutes());
        response.setNotes(session.getNotes());

        if (session.getWorkoutId() != null) {
            WorkoutDay workoutDay = workoutDayRepository.findById(session.getWorkoutId()).orElse(null);

            if (workoutDay != null) {
                response.setWorkoutName(workoutDay.getName());
            }
        }
        return response;
    }

    private <T> T handleCircuitBreakerFallback(Throwable t) {
        if (t instanceof RuntimeException re) {
            throw re;
        }
        throw new RuntimeException("Serviço temporariamente indisponível", t);
    }

}
