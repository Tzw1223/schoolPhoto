package com.example.helloworld.InternetInterface;

import com.example.helloworld.Bean.RequestBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RegisertRequestInterface {
    @GET("user/save")
    Call<RequestBean> register(@Query("account") String account, @Query("password") String password);
}
