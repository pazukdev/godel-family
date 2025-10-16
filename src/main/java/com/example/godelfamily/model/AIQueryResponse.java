package com.example.godelfamily.model;

public class AIQueryResponse {
    private String question;
    private String answer;

    public AIQueryResponse() {
    }

    public AIQueryResponse(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
