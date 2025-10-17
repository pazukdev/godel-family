package com.example.godelfamily.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleEmployeeNotFound() {
        EmployeeNotFoundException exception = new EmployeeNotFoundException(123L);

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleEmployeeNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Employee not found with id: 123", response.getBody().get("error"));
    }

    @Test
    void testHandleValidationExceptions() throws NoSuchMethodException {
        // Create a mock MethodArgumentNotValidException
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "employee");
        bindingResult.addError(new FieldError("employee", "name", "Name is required"));
        bindingResult.addError(new FieldError("employee", "position", "Position is required"));

        MethodParameter parameter = new MethodParameter(
            this.getClass().getDeclaredMethod("dummyMethod", Object.class), 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Name is required", response.getBody().get("name"));
        assertEquals("Position is required", response.getBody().get("position"));
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Unexpected error occurred");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("error").contains("An unexpected error occurred"));
        assertTrue(response.getBody().get("error").contains("Unexpected error occurred"));
    }

    // Dummy method for MethodParameter creation
    public void dummyMethod(Object obj) {
    }
}
