package com.example.godelfamily.controller;

import com.example.godelfamily.model.AIQueryRequest;
import com.example.godelfamily.model.AIQueryResponse;
import com.example.godelfamily.service.EmployeeAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIQueryController {

    private final EmployeeAIService aiService;

    public AIQueryController(EmployeeAIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/query")
    public ResponseEntity<AIQueryResponse> queryEmployees(@RequestBody AIQueryRequest request) {
        try {
            String answer = aiService.queryEmployees(request.getQuestion());
            AIQueryResponse response = new AIQueryResponse(request.getQuestion(), answer);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            AIQueryResponse errorResponse = new AIQueryResponse(
                request.getQuestion(),
                "Sorry, I encountered an error processing your question. Please try again."
            );
            return ResponseEntity.ok(errorResponse);
        }
    }
}

