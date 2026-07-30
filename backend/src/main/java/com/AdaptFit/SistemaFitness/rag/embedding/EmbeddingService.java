package com.AdaptFit.SistemaFitness.rag.embedding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
@Slf4j
public class EmbeddingService {

    @Value("${rag.embedding.url:http://localhost:11434}")
    private String ollamaUrl;

    @Value("${rag.embedding.model:nomic-embed-text}")
    private String embeddingModel;

    private final RestTemplate restTemplate;

    public EmbeddingService() {
        this.restTemplate = new RestTemplate();
    }

    public float[] generateEmbedding(String text) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("model", embeddingModel);
            request.put("input", text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = ollamaUrl + "/api/embed";
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Object embeddingObj = body.get("embedding");
                
                if (embeddingObj instanceof List) {
                    List<Number> embeddingList = (List<Number>) embeddingObj;
                    float[] embedding = new float[embeddingList.size()];
                    for (int i = 0; i < embeddingList.size(); i++) {
                        embedding[i] = embeddingList.get(i).floatValue();
                    }
                    return embedding;
                }
            }

            log.warn("Failed to get embedding from Ollama, response: {}", response);
            return null;

        } catch (Exception e) {
            log.error("Error generating embedding: {}", e.getMessage());
            return null;
        }
    }

    public List<float[]> generateEmbeddings(List<String> texts) {
        List<float[]> embeddings = new ArrayList<>();
        for (String text : texts) {
            float[] embedding = generateEmbedding(text);
            if (embedding != null) {
                embeddings.add(embedding);
            }
        }
        return embeddings;
    }

    public boolean isAvailable() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(ollamaUrl + "/api/tags", String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ensureModelIsLoaded();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("Ollama not available: {}", e.getMessage());
            return false;
        }
    }

    private void ensureModelIsLoaded() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> request = new HashMap<>();
            request.put("name", embeddingModel);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    ollamaUrl + "/api/pull",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Model {} is ready", embeddingModel);
            }
        } catch (Exception e) {
            log.debug("Model check/load: {}", e.getMessage());
        }
    }
}
