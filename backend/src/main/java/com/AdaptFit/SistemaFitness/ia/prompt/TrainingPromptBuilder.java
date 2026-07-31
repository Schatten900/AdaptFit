package com.AdaptFit.SistemaFitness.ia.prompt;

import com.AdaptFit.SistemaFitness.deterministic.evolution.EvolutionLog;
import com.AdaptFit.SistemaFitness.feedback.Feedback;
import com.AdaptFit.SistemaFitness.ia.context.TrainingContext;
import com.AdaptFit.SistemaFitness.ia.retriever.ExerciseRetriever;
import com.AdaptFit.SistemaFitness.preferences.UserPreferences;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.AdaptFit.SistemaFitness.deterministic.workout.WorkoutPlan;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.session.WorkoutSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingPromptBuilder {

    private final ExerciseRetriever exerciseRetriever;
    private final ObjectMapper objectMapper;

    public String buildSystemPrompt() {
        return """
                Você é um treinador especializado em musculação e periodização de treinos.
                
                Regras:
                - Nunca invente exercícios que não estejam no contexto fornecido.
                - Nunca faça cálculos de carga, progressão ou deload — isso é responsabilidade do sistema.
                - Utilize apenas as informações fornecidas no contexto do usuário.
                - Sempre que possível, utilize os exercícios recuperados pelo sistema de busca (RAG).
                - Responda de forma clara, objetiva e educativa.
                - Caso não saiba responder, informe que não possui informações suficientes.
                - Recomende que o usuário consulte um profissional de educação física para lesões ou dores.
                """;
    }

    public String buildContextString(TrainingContext ctx) {
        StringBuilder sb = new StringBuilder();

        Profile profile = ctx.getProfile();
        sb.append("--- Perfil do Usuário ---\n");
        sb.append("Idade: ").append(profile.getAge()).append("\n");
        sb.append("Peso: ").append(profile.getWeight()).append(" kg\n");
        sb.append("Altura: ").append(profile.getHeight()).append(" m\n");
        sb.append("Objetivo: ").append(profile.getGoal() != null ? profile.getGoal().name() : "Não definido").append("\n");
        sb.append("Experiência: ").append(profile.getExperience() != null ? profile.getExperience().name() : "Não definido").append("\n");
        sb.append("Dias por semana: ").append(profile.getDaysPerWeek()).append("\n\n");

        UserPreferences prefs = ctx.getPreferences();
        if (prefs != null) {
            sb.append("--- Preferências do Usuário ---\n");
            if (prefs.getAvailableEquipment() != null && !prefs.getAvailableEquipment().isBlank()) {
                sb.append("Equipamentos disponíveis: ").append(prefs.getAvailableEquipment()).append("\n");
            }
            if (prefs.getInjuries() != null && !prefs.getInjuries().isBlank()) {
                sb.append("Lesões: ").append(prefs.getInjuries()).append("\n");
            }
            if (prefs.getExerciseBlacklist() != null && !prefs.getExerciseBlacklist().isBlank()) {
                sb.append("Exercícios a evitar: ").append(prefs.getExerciseBlacklist()).append("\n");
            }
            sb.append("\n");
        }

        if (ctx.getCurrentPlan() != null) {
            sb.append("--- Plano de Treino Atual ---\n");
            sb.append("Divisão: ").append(ctx.getCurrentPlan().getWorkoutSplit()).append("\n");
            sb.append("Dias por semana: ").append(ctx.getCurrentPlan().getDaysPerWeek()).append("\n");
        }
        sb.append("\n");

        if (ctx.getHistory() != null && !ctx.getHistory().isEmpty()) {
            sb.append("--- Histórico de Treinos (últimos 10) ---\n");
            ctx.getHistory().stream().limit(10).forEach(s -> {
                sb.append("- ").append(s.getSessionDate()).append(": ");
                sb.append(s.getDurationMinutes()).append(" min, ");
                sb.append("volume: ").append(s.getTotalVolume() != null ? String.format("%.0f", s.getTotalVolume()) : "N/A");
                sb.append(" kg\n");
            });
            sb.append("\n");
        }

        if (ctx.getFeedbacks() != null && !ctx.getFeedbacks().isEmpty()) {
            sb.append("--- Feedbacks Recentes (últimos 5) ---\n");
            ctx.getFeedbacks().stream().limit(5).forEach(f -> {
                sb.append("- Fadiga: ").append(f.getFatigueLevel()).append("/10, ");
                sb.append("Dor muscular: ").append(f.getMuscleSoreness()).append("/10");
                if (f.getNotes() != null && !f.getNotes().isBlank()) {
                    sb.append(", Obs: ").append(f.getNotes());
                }
                sb.append("\n");
            });
            sb.append("\n");
        }

        if (ctx.getEvolution() != null && !ctx.getEvolution().isEmpty()) {
            sb.append("--- Evolução (últimos 5 registros) ---\n");
            ctx.getEvolution().stream().limit(5).forEach(e -> {
                sb.append("- ").append(e.getParameterName()).append(": ");
                sb.append(e.getPreviousValue()).append(" → ").append(e.getNewValue());
                sb.append(" (").append(e.getAdjustmentReason()).append(")\n");
            });
            sb.append("\n");
        }

        if (ctx.getRagDocuments() != null && !ctx.getRagDocuments().isEmpty()) {
            sb.append("--- Exercícios Relacionados (RAG) ---\n");
            sb.append(exerciseRetriever.formatExercises(ctx.getRagDocuments())).append("\n\n");
        }

        return sb.toString();
    }
}
