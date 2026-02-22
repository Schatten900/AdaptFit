package com.AdaptFit.SistemaFitness.workout.session;

import com.AdaptFit.SistemaFitness.workout.dto.Session.CreateWorkoutSessionRequest;
import com.AdaptFit.SistemaFitness.workout.dto.Evolution.DashboardResponse;
import com.AdaptFit.SistemaFitness.workout.dto.History.HistoricoResponse;
import com.AdaptFit.SistemaFitness.workout.dto.Session.WorkoutSessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutSessionController.class)
@AutoConfigureMockMvc(addFilters = false)
class WorkoutSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutSessionService workoutSessionService;

    @Test
    void createWorkoutSession_Success() throws Exception {
        WorkoutSessionResponse resp = new WorkoutSessionResponse();
        resp.setId(1L);
        resp.setWorkoutDayId(5L);
        resp.setSessionDate(new Date());
        resp.setDurationMinutes(45);
        resp.setNotes("test");

        when(workoutSessionService.createWorkoutSession(any(CreateWorkoutSessionRequest.class))).thenReturn(resp);

        String json = "{\"workoutDayId\":5,\"sessionDate\":\"2026-02-09T10:00:00\",\"durationMinutes\":45,\"notes\":\"test\"}";

        mockMvc.perform(post("/workout-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.workoutDayId").value(5))
                .andExpect(jsonPath("$.data.notes").value("test"));
    }

    @Test
    void createWorkoutSession_ValidationError() throws Exception {
        when(workoutSessionService.createWorkoutSession(any(CreateWorkoutSessionRequest.class)))
                .thenThrow(new IllegalArgumentException("WorkoutDay ID and session date are required"));

        String json = "{\"workoutDayId\":5,\"sessionDate\":\"2026-02-09T10:00:00\",\"durationMinutes\":45,\"notes\":\"test\"}";

        mockMvc.perform(post("/workout-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("WorkoutDay ID and session date are required"));
    }

    @Test
    void getWorkoutHistory_Success() throws Exception {
        WorkoutSessionResponse resp = new WorkoutSessionResponse();
        resp.setId(2L);
        resp.setWorkoutDayId(6L);
        resp.setSessionDate(new Date());
        resp.setDurationMinutes(30);
        resp.setNotes("desc");

        List<WorkoutSessionResponse> list = Arrays.asList(resp);
        when(workoutSessionService.getWorkoutSessionsForCurrentUser()).thenReturn(list);

        mockMvc.perform(get("/workout-sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(2));
    }

    @Test
    void getWorkoutSession_ById_Success() throws Exception {
        WorkoutSessionResponse resp = new WorkoutSessionResponse();
        resp.setId(3L);
        resp.setWorkoutDayId(7L);
        resp.setSessionDate(new Date());
        resp.setDurationMinutes(60);
        resp.setNotes("note");

        when(workoutSessionService.getWorkoutSessionById(3L)).thenReturn(resp);

        mockMvc.perform(get("/workout-sessions/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(3));
    }

    @Test
    void deleteWorkoutSession_Success() throws Exception {
        doNothing().when(workoutSessionService).deleteWorkoutSession(4L);

        mockMvc.perform(delete("/workout-sessions/4"))
                .andExpect(status().isOk());
    }

    @Test
    void getHistory_Success() throws Exception {
        HistoricoResponse resp = new HistoricoResponse();
        resp.setSessionId(1L);
        resp.setWorkoutDayId(5L);
        resp.setWorkoutName("Treino A");
        resp.setSessionDate(new Date());
        resp.setDurationMinutes(45);
        resp.setExercises(Collections.emptyList());

        when(workoutSessionService.getHistory(anyString(), any(), nullable(Long.class), nullable(Long.class))).thenReturn(Arrays.asList(resp));

        mockMvc.perform(get("/workout-sessions/history")
                .param("period", "month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].sessionId").value(1))
                .andExpect(jsonPath("$.data[0].workoutName").value("Treino A"));
    }

    @Test
    void getDashboard_Success() throws Exception {
        DashboardResponse resp = new DashboardResponse();
        resp.setTotalSessions(10);
        resp.setTotalDurationMinutes(450);
        resp.setAverageDuration(45.0);
        resp.setSessionsByMuscleGroup(Collections.emptyMap());
        resp.setEvolutionData(Collections.emptyList());
        resp.setWorkoutDistribution(Collections.emptyMap());

        when(workoutSessionService.getDashboard(anyString(), any(), nullable(Long.class), nullable(Long.class))).thenReturn(resp);

        mockMvc.perform(get("/workout-sessions/dashboard")
                .param("period", "month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalSessions").value(10))
                .andExpect(jsonPath("$.data.totalDurationMinutes").value(450));
    }
}
