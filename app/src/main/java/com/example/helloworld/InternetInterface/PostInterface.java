package com.example.helloworld.InternetInterface;

import com.example.helloworld.Bean.PostBean;
import com.example.helloworld.Bean.RequestBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostInterface {
    @POST("feedback/save")
    Call<RequestBean>postNews(@Body RequestBody route);//传入的参数为RequestBody
}
