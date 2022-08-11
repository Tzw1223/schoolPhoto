package com.example.helloworld.InternetInterface;

import com.example.helloworld.Bean.FeedbackBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FeedbackRequestInterface {
    @GET("feedback/get")
    Call<FeedbackBean>getFeedback(@Query("account") String account);
}
