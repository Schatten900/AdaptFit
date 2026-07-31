package com.AdaptFit.SistemaFitness.rag.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Slf4j
public class QdrantService {

    @Value("${rag.qdrant.url:http://localhost:6333}")
    private String qdrantUrl;

    public static final String RECIPES_COLLECTION = "recipes";
    public static final String EXERCISES_COLLECTION = "exercises";
    public static final int VECTOR_SIZE = 768;

    private final RestTemplate restTemplate;

    public QdrantService() {
        this.restTemplate = new RestTemplate();
    }

    public void createCollectionIfNotExists(String collectionName, int vectorSize) {
        try {
            if (collectionExists(collectionName)) {
                log.info("Collection {} already exists", collectionName);
                return;
            }

            Map<String, Object> request = new HashMap<>();
            Map<String, Object> vectorsConfig = new HashMap<>();
            vectorsConfig.put("size", vectorSize);
            vectorsConfig.put("distance", "Cosine");
            request.put("vectors", vectorsConfig);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = qdrantUrl + "/collections/" + collectionName;
            restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            log.info("Created collection: {}", collectionName);
        } catch (Exception e) {
            log.error("Error creating collection {}: {}", collectionName, e.getMessage());
        }
    }

    public void upsertPoints(String collectionName, List<Map<String, Object>> points) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("points", points);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = qdrantUrl + "/collections/" + collectionName + "/points?wait=true";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            log.info("Upserted {} points to {}: {}", points.size(), collectionName, response.getStatusCode());
        } catch (Exception e) {
            log.error("Error upserting points to {}: {}", collectionName, e.getMessage());
        }
    }

    public List<Map<String, Object>> search(String collectionName, float[] vector, int limit) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("vector", vector);
            request.put("limit", limit);
            request.put("with_payload", true);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            String url = qdrantUrl + "/collections/" + collectionName + "/points/search";
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("result");
                return results != null ? results : new ArrayList<>();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Error searching in {}: {}", collectionName, e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean collectionExists(String collectionName) {
        try {
            String url = qdrantUrl + "/collections/" + collectionName;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteCollection(String collectionName) {
        try {
            String url = qdrantUrl + "/collections/" + collectionName;
            restTemplate.delete(url);
            log.info("Deleted collection: {}", collectionName);
        } catch (Exception e) {
            log.error("Error deleting collection {}: {}", collectionName, e.getMessage());
        }
    }
}
