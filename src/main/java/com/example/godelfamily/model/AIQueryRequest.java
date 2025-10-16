package com.example.godelfamily.model;

public class AIQueryRequest {
    private String question;

    public AIQueryRequest() {
    }

    public AIQueryRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

