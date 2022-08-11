package com.example.helloworld.InternetInterface;

import com.example.helloworld.Bean.RequestBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginRequestInterface {
    @GET("user/login")
    Call<RequestBean>login(@Query("account") String account, @Query("password") String password);

}
