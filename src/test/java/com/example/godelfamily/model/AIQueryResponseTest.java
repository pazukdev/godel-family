package com.example.godelfamily.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AIQueryResponseTest {

    @Test
    void testNoArgsConstructor() {
        AIQueryResponse response = new AIQueryResponse();
        assertNotNull(response);
        assertNull(response.getQuestion());
        assertNull(response.getAnswer());
    }

    @Test
    void testAllArgsConstructor() {
        AIQueryResponse response = new AIQueryResponse("How many developers?", "5 developers");

        assertEquals("How many developers?", response.getQuestion());
        assertEquals("5 developers", response.getAnswer());
    }

    @Test
    void testSettersAndGetters() {
        AIQueryResponse response = new AIQueryResponse();

        response.setQuestion("Who are the seniors?");
        response.setAnswer("Pavel and Dmitry");

        assertEquals("Who are the seniors?", response.getQuestion());
        assertEquals("Pavel and Dmitry", response.getAnswer());
    }
}

