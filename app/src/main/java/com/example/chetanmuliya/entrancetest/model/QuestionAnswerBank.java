package com.example.chetanmuliya.entrancetest.model;

/**
 * Created by chetanmuliya on 12/14/2017.
 */

public class QuestionAnswerBank {

    private String question;
    private String[] options=new String[5];
    private String answer;

    public QuestionAnswerBank(String question, String[] options, String answer) {
        this.question = question;
        this.options[0] = options[0];
        this.options[1] = options[1];
        this.options[2] = options[2];
        this.options[3] = options[3];
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions(int i) {
        return  options[i];
    }

    public void setOptions(int i,String value) {
        this.options[i] = value;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
