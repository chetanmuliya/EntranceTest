package com.example.chetanmuliya.entrancetest.rest;

import com.example.chetanmuliya.entrancetest.model.QuestionModel;
import com.example.chetanmuliya.entrancetest.model.QuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chetanmuliya on 12/14/2017.
 */

public interface ApiInterface {

    @GET("test_qa.php")
    Call<QuestionModel> getQuestionAnswer();
}
