package com.example.godelfamily.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        Long id = 123L;
        EmployeeNotFoundException exception = new EmployeeNotFoundException(id);

        assertEquals("Employee not found with id: 123", exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        EmployeeNotFoundException exception = new EmployeeNotFoundException(1L);
        assertTrue(exception instanceof RuntimeException);
    }
}

