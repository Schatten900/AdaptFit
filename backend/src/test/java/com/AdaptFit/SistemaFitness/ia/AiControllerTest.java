package com.AdaptFit.SistemaFitness.ia;

import com.AdaptFit.SistemaFitness.ia.agents.NutritionAgent;
import com.AdaptFit.SistemaFitness.ia.agents.TrainingAgent;
import com.AdaptFit.SistemaFitness.ia.dto.ChatRequest;
import com.AdaptFit.SistemaFitness.jwt.JwtAuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiController.class)
@AutoConfigureMockMvc(addFilters = false)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgentService agentService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void trainingChat_Success() throws Exception {
        TrainingAgent.TrainingAgentResult result = new TrainingAgent.TrainingAgentResult(
                "Ótima pergunta! Com base no seu perfil...", true, null);
        when(agentService.processTrainingQuestion(anyString())).thenReturn(result);

        ChatRequest request = new ChatRequest();
        request.setQuestion("Como posso melhorar meu treino de peito?");

        mockMvc.perform(post("/api/v1/ai/training/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("Ótima pergunta! Com base no seu perfil..."))
                .andExpect(jsonPath("$.data.success").value(true));
    }

    @Test
    void nutritionChat_Success() throws Exception {
        NutritionAgent.NutritionAgentResult result = new NutritionAgent.NutritionAgentResult(
                "Baseado no seu plano nutricional...", true, null);
        when(agentService.processNutritionQuestion(anyString())).thenReturn(result);

        ChatRequest request = new ChatRequest();
        request.setQuestion("Quais refeições são boas para o pré-treino?");

        mockMvc.perform(post("/api/v1/ai/nutrition/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("Baseado no seu plano nutricional..."))
                .andExpect(jsonPath("$.data.success").value(true));
    }

    @Test
    void trainingChat_ValidationError_EmptyQuestion() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setQuestion("");

        mockMvc.perform(post("/api/v1/ai/training/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void trainingChat_ErrorResponse() throws Exception {
        TrainingAgent.TrainingAgentResult result = new TrainingAgent.TrainingAgentResult(
                "", false, "Erro ao comunicar com o modelo");
        when(agentService.processTrainingQuestion(anyString())).thenReturn(result);

        ChatRequest request = new ChatRequest();
        request.setQuestion("Teste");

        mockMvc.perform(post("/api/v1/ai/training/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.success").value(false))
                .andExpect(jsonPath("$.data.errorMessage").value("Erro ao comunicar com o modelo"));
    }

    @Test
    void healthCheck_Success() throws Exception {
        when(agentService.isLlmAvailable()).thenReturn(true);

        mockMvc.perform(get("/api/v1/ai/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void healthCheck_Unavailable() throws Exception {
        when(agentService.isLlmAvailable()).thenReturn(false);

        mockMvc.perform(get("/api/v1/ai/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }
}
