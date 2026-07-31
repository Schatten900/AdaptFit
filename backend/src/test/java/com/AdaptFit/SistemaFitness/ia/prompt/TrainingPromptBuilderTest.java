package com.AdaptFit.SistemaFitness.ia.prompt;

import com.AdaptFit.SistemaFitness.ia.context.TrainingContext;
import com.AdaptFit.SistemaFitness.ia.retriever.ExerciseRetriever;
import com.AdaptFit.SistemaFitness.profile.Profile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainingPromptBuilderTest {

    private TrainingPromptBuilder promptBuilder;

    @Mock
    private ExerciseRetriever exerciseRetriever;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        promptBuilder = new TrainingPromptBuilder(exerciseRetriever, objectMapper);
    }

    @Test
    void buildSystemPrompt_ShouldReturnNonEmpty() {
        String prompt = promptBuilder.buildSystemPrompt();
        assertNotNull(prompt);
        assertFalse(prompt.isBlank());
        assertTrue(prompt.contains("treinador"));
        assertTrue(prompt.contains("Nunca invente exercícios"));
    }

    @Test
    void buildContextString_ShouldIncludeProfileInfo() {
        Profile profile = new Profile();
        profile.setAge(25);
        profile.setWeight(new BigDecimal("80"));
        profile.setHeight(new BigDecimal("1.75"));

        TrainingContext ctx = TrainingContext.builder()
                .profile(profile)
                .preferences(null)
                .currentPlan(null)
                .history(Collections.emptyList())
                .feedbacks(Collections.emptyList())
                .evolution(Collections.emptyList())
                .ragDocuments(Collections.emptyList())
                .userQuestion("teste")
                .build();

        String context = promptBuilder.buildContextString(ctx);
        assertNotNull(context);
        assertTrue(context.contains("25"));
        assertTrue(context.contains("80"));
    }
}
