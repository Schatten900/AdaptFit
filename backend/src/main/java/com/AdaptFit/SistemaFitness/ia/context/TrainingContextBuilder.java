package com.AdaptFit.SistemaFitness.ia.context;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLogRepository;
import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutPlan;
import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutPlanRepository;
import com.AdaptFit.SistemaFitness.feedback.Feedback;
import com.AdaptFit.SistemaFitness.feedback.FeedbackRepository;
import com.AdaptFit.SistemaFitness.ia.retriever.ExerciseRetriever;
import com.AdaptFit.SistemaFitness.preferences.PreferencesRepository;
import com.AdaptFit.SistemaFitness.preferences.UserPreferences;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.AdaptFit.SistemaFitness.profile.ProfileRepository;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSession;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingContextBuilder {

    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final PreferencesRepository preferencesRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final FeedbackRepository feedbackRepository;
    private final EvolutionLogRepository evolutionLogRepository;
    private final ExerciseRetriever exerciseRetriever;

    public TrainingContext build(String userQuestion) {
        Long userId = userService.getCurrentUserId();

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Perfil não encontrado. Complete seu perfil primeiro."));

        UserPreferences preferences = preferencesRepository.findByUserId(userId).orElse(null);

        WorkoutPlan currentPlan = workoutPlanRepository.findByUserIdAndIsActiveTrue(userId).orElse(null);

        List<WorkoutSession> history = workoutSessionRepository.findByUserIdOrderBySessionDateDesc(userId);

        List<Feedback> feedbacks = feedbackRepository.findByUserIdOrderByCreatedAtDesc(userId);

        List<EvolutionLog> evolution = evolutionLogRepository.findByUserIdOrderByCreatedAtDesc(userId);

        List<ExerciseCatalog> ragDocuments = exerciseRetriever.retrieveExercises(userQuestion, 10);

        log.info("Training context built for user {}: profile={}, plan={}, history={}, feedbacks={}, evolution={}, rag={}",
                userId, profile != null, currentPlan != null, history.size(), feedbacks.size(), evolution.size(), ragDocuments.size());

        return TrainingContext.builder()
                .profile(profile)
                .preferences(preferences)
                .currentPlan(currentPlan)
                .history(history)
                .feedbacks(feedbacks)
                .evolution(evolution)
                .ragDocuments(ragDocuments)
                .userQuestion(userQuestion)
                .build();
    }
}
