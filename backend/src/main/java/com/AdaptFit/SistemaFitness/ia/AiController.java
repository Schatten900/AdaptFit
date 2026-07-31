package com.AdaptFit.SistemaFitness.ia;

import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import com.AdaptFit.SistemaFitness.ia.agents.NutritionAgent;
import com.AdaptFit.SistemaFitness.ia.agents.TrainingAgent;
import com.AdaptFit.SistemaFitness.ia.dto.ChatRequest;
import com.AdaptFit.SistemaFitness.ia.dto.ChatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Validated
public class AiController {

    private final AgentService agentService;

    @PostMapping("/training/chat")
    public ResponseEntity<ApiResponse<ChatResponse>> trainingChat(@Valid @RequestBody ChatRequest request) {
        TrainingAgent.TrainingAgentResult result = agentService.processTrainingQuestion(request.getQuestion());
        ChatResponse response = new ChatResponse(result.content(), result.success(), result.errorMessage());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/nutrition/chat")
    public ResponseEntity<ApiResponse<ChatResponse>> nutritionChat(@Valid @RequestBody ChatRequest request) {
        NutritionAgent.NutritionAgentResult result = agentService.processNutritionQuestion(request.getQuestion());
        ChatResponse response = new ChatResponse(result.content(), result.success(), result.errorMessage());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Boolean>> health() {
        boolean available = agentService.isLlmAvailable();
        return ResponseEntity.ok(ApiResponse.success(available));
    }
}
