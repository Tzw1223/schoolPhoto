package com.example.helloworld.InternetInterface;

import com.example.helloworld.Bean.NewsBean;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsRequestInterface {
    @GET("news/get")
    Call<NewsBean>getNews();
}
