package com.example.chetanmuliya.entrancetest.model;

import java.util.List;

/**
 * Created by chetanmuliya on 12/14/2017.
 */

public class QuestionResponse {
    List<QuestionAnswerBank> quizList;

    public QuestionResponse(List<QuestionAnswerBank> quizList) {
        this.quizList = quizList;
    }

    public List<QuestionAnswerBank> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<QuestionAnswerBank> quizList) {
        this.quizList = quizList;
    }
}
