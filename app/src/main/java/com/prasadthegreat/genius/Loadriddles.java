package com.prasadthegreat.genius;

public class Loadriddles {
    public  String answer,question;

    public Loadriddles(String answer, String question) {
        this.answer = answer;
        this.question = question;
    }
    public Loadriddles(){}

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
