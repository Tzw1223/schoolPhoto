package com.example.helloworld.InternetInterface;

import com.example.helloworld.Bean.EvaluateBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EvaluateService {
    @POST("feedback/evaluate")
    Call<EvaluateBean> postEva(@Body RequestBody body);
}
