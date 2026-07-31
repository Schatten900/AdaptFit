package com.AdaptFit.SistemaFitness.ia;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmClient {

    private final LlmProperties llmProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public LlmResponse sendPrompt(String systemPrompt, String userPrompt, String context) {
        return sendPrompt(systemPrompt, userPrompt, context, llmProperties.getModel(), llmProperties.getTemperature());
    }

    public LlmResponse sendPrompt(String systemPrompt, String userPrompt, String context, String model, Double temperature) {
        long startTime = System.currentTimeMillis();
        String fullPrompt = buildFullPrompt(systemPrompt, userPrompt, context);

        try {
            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            request.put("prompt", fullPrompt);
            request.put("temperature", temperature);
            request.put("stream", false);

            if (llmProperties.getMaxTokens() != null) {
                request.put("options", Map.of("num_predict", llmProperties.getMaxTokens()));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = llmProperties.getBaseUrl() + "/api/generate";
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            long latencyMs = System.currentTimeMillis() - startTime;

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String content = (String) response.getBody().get("response");
                Integer tokensIn = extractInt(response.getBody(), "prompt_eval_count");
                Integer tokensOut = extractInt(response.getBody(), "eval_count");

                LlmResponse llmResponse = new LlmResponse();
                llmResponse.setContent(content != null ? content : "");
                llmResponse.setModel(model);
                llmResponse.setTemperature(temperature);
                llmResponse.setLatencyMs(latencyMs);
                llmResponse.setTokensIn(tokensIn != null ? tokensIn : 0);
                llmResponse.setTokensOut(tokensOut != null ? tokensOut : 0);
                llmResponse.setFullPrompt(fullPrompt);
                llmResponse.setSuccess(true);

                log.info("LLM response received in {}ms (tokens: in={}, out={})", latencyMs, llmResponse.getTokensIn(), llmResponse.getTokensOut());
                return llmResponse;
            }

            log.warn("LLM returned non-OK status: {}", response.getStatusCode());
            return createErrorResponse(fullPrompt, model, temperature, latencyMs, "LLM returned status " + response.getStatusCode());

        } catch (ResourceAccessException e) {
            long latencyMs = System.currentTimeMillis() - startTime;
            log.error("LLM timeout after {}ms: {}", latencyMs, e.getMessage());
            return createErrorResponse(fullPrompt, model, temperature, latencyMs, "Timeout ao comunicar com o modelo");
        } catch (Exception e) {
            long latencyMs = System.currentTimeMillis() - startTime;
            log.error("LLM error after {}ms: {}", latencyMs, e.getMessage());
            return createErrorResponse(fullPrompt, model, temperature, latencyMs, "Erro ao comunicar com o modelo: " + e.getMessage());
        }
    }

    public boolean isAvailable() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    llmProperties.getBaseUrl() + "/api/tags", String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("Ollama not available: {}", e.getMessage());
            return false;
        }
    }

    private String buildFullPrompt(String systemPrompt, String userPrompt, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append(systemPrompt).append("\n\n");
        if (context != null && !context.isBlank()) {
            sb.append("### Contexto do Usuário\n\n").append(context).append("\n\n");
        }
        sb.append("### Pergunta do Usuário\n\n").append(userPrompt).append("\n\n");
        sb.append("### Instruções\n\n");
        sb.append("- Responda em português.\n");
        sb.append("- Utilize apenas as informações fornecidas no contexto.\n");
        sb.append("- Não invente dados que não estejam no contexto.\n");
        sb.append("- Se não souber responder, informe que não possui informações suficientes.\n");
        return sb.toString();
    }

    private LlmResponse createErrorResponse(String fullPrompt, String model, Double temperature, long latencyMs, String errorMessage) {
        LlmResponse response = new LlmResponse();
        response.setContent("");
        response.setModel(model);
        response.setTemperature(temperature);
        response.setLatencyMs(latencyMs);
        response.setTokensIn(0);
        response.setTokensOut(0);
        response.setFullPrompt(fullPrompt);
        response.setSuccess(false);
        response.setErrorMessage(errorMessage);
        return response;
    }

    private Integer extractInt(Map<String, Object> body, String key) {
        Object value = body.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

}
