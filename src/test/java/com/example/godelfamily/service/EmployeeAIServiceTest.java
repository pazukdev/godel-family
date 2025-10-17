package com.example.godelfamily.service;

import com.example.godelfamily.model.Employee;
import com.example.godelfamily.model.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeAIServiceTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeAIService employeeAIService;

    private List<Employee> sampleEmployees;

    @BeforeEach
    void setUp() {
        sampleEmployees = Arrays.asList(
            new Employee(1L, "John", "Developer", Title.SENIOR, "Java"),
            new Employee(2L, "Jane", "QA", Title.MIDDLE, "QA"),
            new Employee(3L, "Bob", "BA", Title.JUNIOR, "BA")
        );
    }

    @Test
    void testQueryEmployees_NoApiKey() {
        ReflectionTestUtils.setField(employeeAIService, "openAiApiKey", null);

        String result = employeeAIService.queryEmployees("How many developers?");

        assertEquals("OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable.", result);
        verify(employeeService, never()).getAllEmployees();
    }

    @Test
    void testQueryEmployees_EmptyApiKey() {
        ReflectionTestUtils.setField(employeeAIService, "openAiApiKey", "");

        String result = employeeAIService.queryEmployees("How many developers?");

        assertEquals("OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable.", result);
        verify(employeeService, never()).getAllEmployees();
    }

    @Test
    void testQueryEmployees_WithApiKey_HandlesException() {
        ReflectionTestUtils.setField(employeeAIService, "openAiApiKey", "fake-api-key");
        when(employeeService.getAllEmployees()).thenReturn(sampleEmployees);

        String result = employeeAIService.queryEmployees("How many developers?");

        // Should return error message because the API key is fake
        assertTrue(result.startsWith("Error processing your question:"));
        assertTrue(result.contains("Please check your OpenAI API key and try again."));
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testQueryEmployees_WithNullApiKey_ReturnsConfigMessage() {
        // Default is null
        String result = employeeAIService.queryEmployees("Test question");

        assertEquals("OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable.", result);
    }
}

