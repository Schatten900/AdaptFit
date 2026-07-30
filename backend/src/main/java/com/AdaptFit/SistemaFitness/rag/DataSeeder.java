package com.AdaptFit.SistemaFitness.rag;

import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalog;
import com.AdaptFit.SistemaFitness.workout.exercise.catalog.ExerciseCatalogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final ExerciseCatalogRepository exerciseRepository;

    @Value("classpath:RAG/workout/exercicios.json")
    private Resource exerciciosJson;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            if (exerciseRepository.count() == 0) {
                log.info("Seeding exercise catalog from exercicios.json...");
                ObjectMapper mapper = new ObjectMapper();
                JsonNode exercises = mapper.readTree(exerciciosJson.getInputStream());

                List<ExerciseCatalog> entities = new ArrayList<>();

                for (JsonNode node : exercises) {
                    ExerciseCatalog exercise = new ExerciseCatalog();
                    exercise.setName(node.get("nome").asText());
                    exercise.setDescription(node.has("descricao") ? node.get("descricao").asText() : null);
                    exercise.setPrimaryMuscle(node.has("musculo_principal") ? node.get("musculo_principal").asText() : null);

                    if (node.has("musculos_secundarios")) {
                        List<String> secondary = new ArrayList<>();
                        node.get("musculos_secundarios").forEach(s -> secondary.add(s.asText()));
                        exercise.setSecondaryMuscles(mapper.writeValueAsString(secondary));
                    }

                    boolean isBodyweight = false;
                    if (node.has("equipamentos")) {
                        for (JsonNode eq : node.get("equipamentos")) {
                            if ("Peso Corporal".equalsIgnoreCase(eq.asText())) {
                                isBodyweight = true;
                                break;
                            }
                        }
                    }
                    exercise.setIsBodyweight(isBodyweight);

                    entities.add(exercise);
                }

                exerciseRepository.saveAll(entities);
                log.info("Seeded {} exercises from exercicios.json", entities.size());
            }
        };
    }
}
