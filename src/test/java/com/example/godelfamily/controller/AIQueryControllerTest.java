package com.example.godelfamily.controller;

import com.example.godelfamily.model.AIQueryRequest;
import com.example.godelfamily.service.EmployeeAIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AIQueryController.class)
class AIQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeAIService aiService;

    @Test
    void testQueryEmployees_Success() throws Exception {
        AIQueryRequest request = new AIQueryRequest("How many developers?");
        String aiAnswer = "There are 5 developers.";

        when(aiService.queryEmployees("How many developers?")).thenReturn(aiAnswer);

        mockMvc.perform(post("/api/ai/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.question").value("How many developers?"))
            .andExpect(jsonPath("$.answer").value("There are 5 developers."));

        verify(aiService, times(1)).queryEmployees("How many developers?");
    }

    @Test
    void testQueryEmployees_WithException() throws Exception {
        AIQueryRequest request = new AIQueryRequest("Test question");

        when(aiService.queryEmployees(anyString())).thenThrow(new RuntimeException("AI service error"));

        mockMvc.perform(post("/api/ai/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.question").value("Test question"))
            .andExpect(jsonPath("$.answer").value("Sorry, I encountered an error processing your question. Please try again."));

        verify(aiService, times(1)).queryEmployees("Test question");
    }

    @Test
    void testQueryEmployees_DifferentQuestion() throws Exception {
        AIQueryRequest request = new AIQueryRequest("Who are the seniors?");
        String aiAnswer = "Pavel and Dmitry are seniors.";

        when(aiService.queryEmployees("Who are the seniors?")).thenReturn(aiAnswer);

        mockMvc.perform(post("/api/ai/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.question").value("Who are the seniors?"))
            .andExpect(jsonPath("$.answer").value("Pavel and Dmitry are seniors."));

        verify(aiService, times(1)).queryEmployees("Who are the seniors?");
    }
}

