package com.AdaptFit.SistemaFitness.ia;

import com.AdaptFit.SistemaFitness.ia.agents.NutritionAgent;
import com.AdaptFit.SistemaFitness.ia.agents.TrainingAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private final TrainingAgent trainingAgent;
    private final NutritionAgent nutritionAgent;
    private final LlmClient llmClient;

    public TrainingAgent.TrainingAgentResult processTrainingQuestion(String question) {
        log.info("AgentService routing to TrainingAgent");
        return trainingAgent.processQuestion(question);
    }

    public NutritionAgent.NutritionAgentResult processNutritionQuestion(String question) {
        log.info("AgentService routing to NutritionAgent");
        return nutritionAgent.processQuestion(question);
    }

    public boolean isLlmAvailable() {
        return llmClient.isAvailable();
    }
}
