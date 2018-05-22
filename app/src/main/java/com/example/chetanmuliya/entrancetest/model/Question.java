package com.example.chetanmuliya.entrancetest.model;

/**
 * Created by Abhishek on 11/13/2017.
 */

public class Question {
    private String question;
    private String[] choice=new String[4];
    private String answer;
    private String attempted;



    public Question() {
    }

    public Question(String question, String[] choice, String answer) {
        this.question = question;
        this.choice[0] = choice[0];
        this.choice[1] = choice[1];
        this.choice[2] = choice[2];
        this.choice[3] = choice[3];
        this.answer = answer;
    }

    public String getAttempted() {
        return attempted;
    }

    public void setAttempted(String attempted) {
        this.attempted = attempted;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice(int i) {
        return choice[i];
    }

    public void setChoice(int i,String choice) {
        this.choice[i] = choice;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
