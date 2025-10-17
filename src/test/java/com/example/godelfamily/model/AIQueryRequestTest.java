package com.example.godelfamily.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AIQueryRequestTest {

    @Test
    void testNoArgsConstructor() {
        AIQueryRequest request = new AIQueryRequest();
        assertNotNull(request);
        assertNull(request.getQuestion());
    }

    @Test
    void testAllArgsConstructor() {
        AIQueryRequest request = new AIQueryRequest("How many developers?");
        assertEquals("How many developers?", request.getQuestion());
    }

    @Test
    void testSetterAndGetter() {
        AIQueryRequest request = new AIQueryRequest();
        request.setQuestion("Who are the seniors?");

        assertEquals("Who are the seniors?", request.getQuestion());
    }
}

