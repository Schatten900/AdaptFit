package com.AdaptFit.SistemaFitness.ia.context;

import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutPlan;
import com.AdaptFit.SistemaFitness.feedback.Feedback;
import com.AdaptFit.SistemaFitness.preferences.UserPreferences;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSession;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainingContext {
    private Profile profile;
    private UserPreferences preferences;
    private WorkoutPlan currentPlan;
    private List<WorkoutSession> history;
    private List<Feedback> feedbacks;
    private List<EvolutionLog> evolution;
    private List<ExerciseCatalog> ragDocuments;
    private String userQuestion;
}
