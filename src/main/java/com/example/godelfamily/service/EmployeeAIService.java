package com.example.godelfamily.service;

import com.example.godelfamily.model.Employee;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeAIService {

    private final EmployeeService employeeService;
    private final String openAiApiKey;

    public EmployeeAIService(EmployeeService employeeService,
                             @Value("${openai.api.key:#{null}}") String openAiApiKey) {
        this.employeeService = employeeService;
        this.openAiApiKey = openAiApiKey;
    }

    public String queryEmployees(String question) {
        if (openAiApiKey == null || openAiApiKey.isEmpty()) {
            return "OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable.";
        }

        try {
            // Get all employees to provide context
            List<Employee> employees = employeeService.getAllEmployees();

            // Build context from employee data
            StringBuilder employeeContext = new StringBuilder();
            employeeContext.append("Here is the complete list of employees:\n\n");

            for (Employee emp : employees) {
                employeeContext.append(String.format(
                    "- ID: %d, Name: %s, Position: %s, Title: %s, Division: %s\n",
                    emp.getId(),
                    emp.getName(),
                    emp.getPosition(),
                    emp.getTitle(),
                    emp.getDivision()
                ));
            }

            // Create OpenAI service
            OpenAiService service = new OpenAiService(openAiApiKey, Duration.ofSeconds(60));

            // Create messages for chat completion
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "You are a concise AI assistant for employee data queries. " +
                "Answer questions directly and briefly using only the provided employee data. " +
                "Use short sentences. No explanations unless asked. " +
                "If data is unavailable, say 'No data available' in one sentence."));

            messages.add(new ChatMessage(ChatMessageRole.USER.value(),
                employeeContext.toString() + "\n\nUser Question: " + question));

            // Create chat completion request
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-4o-mini")
                .messages(messages)
                .temperature(0.3)
                .maxTokens(200)
                .build();

            // Call OpenAI API and return response
            String response = service.createChatCompletion(chatCompletionRequest)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();

            service.shutdownExecutor();

            return response;

        } catch (Exception e) {
            return "Error processing your question: " + e.getMessage() +
                   ". Please check your OpenAI API key and try again.";
        }
    }
}
