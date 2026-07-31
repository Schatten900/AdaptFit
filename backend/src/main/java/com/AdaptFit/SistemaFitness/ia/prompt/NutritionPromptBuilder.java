package com.AdaptFit.SistemaFitness.ia.prompt;

import com.AdaptFit.SistemaFitness.deterministic.nutricional.NutritionalPlan;
import com.AdaptFit.SistemaFitness.ia.context.NutritionContext;
import com.AdaptFit.SistemaFitness.ia.retriever.RecipeRetriever;
import com.AdaptFit.SistemaFitness.preferences.UserPreferences;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NutritionPromptBuilder {

    private final RecipeRetriever recipeRetriever;
    private final ObjectMapper objectMapper;

    public String buildSystemPrompt() {
        return """
                Você é um nutricionista especializado em nutrição esportiva e alimentação para健身.
                
                Regras:
                - Nunca invente receitas que não estejam no contexto fornecido.
                - Nunca faça cálculos de macros, calorias ou TMB — isso é responsabilidade do sistema.
                - Utilize apenas as informações fornecidas no contexto do usuário.
                - Sempre que possível, utilize as receitas recuperadas pelo sistema de busca (RAG).
                - Responda de forma clara, objetiva e educativa.
                - Caso não saiba responder, informe que não possui informações suficientes.
                - Recomende que o usuário consulte um nutricionista para dietas restritivas ou condições médicas.
                """;
    }

    public String buildContextString(NutritionContext ctx) {
        StringBuilder sb = new StringBuilder();

        Profile profile = ctx.getProfile();
        sb.append("--- Perfil do Usuário ---\n");
        sb.append("Idade: ").append(profile.getAge()).append("\n");
        sb.append("Peso: ").append(profile.getWeight()).append(" kg\n");
        sb.append("Altura: ").append(profile.getHeight()).append(" m\n");
        sb.append("Objetivo: ").append(profile.getGoal() != null ? profile.getGoal().name() : "Não definido").append("\n");
        sb.append("Gênero: ").append(profile.getGender() != null ? profile.getGender().name() : "Não informado").append("\n\n");

        NutritionalPlan plan = ctx.getNutritionalPlan();
        if (plan != null) {
            sb.append("--- Plano Nutricional Atual ---\n");
            sb.append("Calorias alvo: ").append(String.format("%.0f", plan.getTargetCalories())).append(" kcal\n");
            sb.append("Proteína: ").append(String.format("%.1f", plan.getProteinGrams())).append(" g\n");
            sb.append("Carboidratos: ").append(String.format("%.1f", plan.getCarbsGrams())).append(" g\n");
            sb.append("Gorduras: ").append(String.format("%.1f", plan.getFatGrams())).append(" g\n");
            sb.append("TMB: ").append(String.format("%.0f", plan.getTmb())).append(" kcal\n");
            sb.append("TDEE: ").append(String.format("%.0f", plan.getTdee())).append(" kcal\n\n");
        } else {
            sb.append("--- Plano Nutricional ---\n");
            sb.append("Nenhum plano nutricional ativo encontrado.\n\n");
        }

        UserPreferences prefs = ctx.getPreferences();
        if (prefs != null) {
            if (prefs.getInjuries() != null && !prefs.getInjuries().isBlank()) {
                sb.append("Restrições alimentares: ").append(prefs.getInjuries()).append("\n\n");
            }
        }

        if (ctx.getRagDocuments() != null && !ctx.getRagDocuments().isEmpty()) {
            sb.append("--- Receitas Relacionadas (RAG) ---\n");
            sb.append(recipeRetriever.formatRecipes(ctx.getRagDocuments())).append("\n\n");
        }

        return sb.toString();
    }
}
