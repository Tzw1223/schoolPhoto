package com.example.helloworld.InternetInterface;

import com.example.helloworld.Bean.TraceBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("feedback/process")
    Call<TraceBean> trace(@Query("feed_back_id") int feed_back_id);

}
