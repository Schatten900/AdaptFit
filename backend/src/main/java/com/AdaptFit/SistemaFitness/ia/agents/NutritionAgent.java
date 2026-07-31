package com.AdaptFit.SistemaFitness.ia.agents;

import com.AdaptFit.SistemaFitness.ia.LlmClient;
import com.AdaptFit.SistemaFitness.ia.LlmResponse;
import com.AdaptFit.SistemaFitness.ia.context.NutritionContext;
import com.AdaptFit.SistemaFitness.ia.context.NutritionContextBuilder;
import com.AdaptFit.SistemaFitness.ia.log.AiLogService;
import com.AdaptFit.SistemaFitness.ia.prompt.NutritionPromptBuilder;
import com.AdaptFit.SistemaFitness.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NutritionAgent {

    private final NutritionContextBuilder contextBuilder;
    private final NutritionPromptBuilder promptBuilder;
    private final LlmClient llmClient;
    private final AiLogService aiLogService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public NutritionAgentResult processQuestion(String userQuestion) {
        Long userId = userService.getCurrentUserId();
        log.info("NutritionAgent processing question for user {}: {}", userId, userQuestion);

        NutritionContext context = contextBuilder.build(userQuestion);
        String systemPrompt = promptBuilder.buildSystemPrompt();
        String contextString = promptBuilder.buildContextString(context);

        LlmResponse llmResponse = llmClient.sendPrompt(systemPrompt, userQuestion, contextString);

        String documentsJson = serializeDocuments(context);
        String decision = llmResponse.isSuccess() ? "response_generated" : "error";
        String reason = llmResponse.isSuccess() ? "Pergunta respondida com sucesso" : llmResponse.getErrorMessage();
        Double confidence = llmResponse.isSuccess() ? 0.8 : 0.0;

        aiLogService.logInteraction(userId, "NutritionAgent", llmResponse, documentsJson, decision, reason, confidence);

        return new NutritionAgentResult(llmResponse.getContent(), llmResponse.isSuccess(), llmResponse.getErrorMessage());
    }

    private String serializeDocuments(NutritionContext ctx) {
        try {
            if (ctx.getRagDocuments() != null && !ctx.getRagDocuments().isEmpty()) {
                return objectMapper.writeValueAsString(ctx.getRagDocuments().stream()
                        .map(r -> r.getNome())
                        .toList());
            }
        } catch (Exception e) {
            log.warn("Failed to serialize RAG documents: {}", e.getMessage());
        }
        return "[]";
    }

    public record NutritionAgentResult(String content, boolean success, String errorMessage) {}
}
