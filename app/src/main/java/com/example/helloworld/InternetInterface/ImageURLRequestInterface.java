package com.example.helloworld.InternetInterface;

import android.database.Observable;

import com.example.helloworld.Bean.URLBean;

import org.springframework.mock.web.MockMultipartFile;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImageURLRequestInterface {
    @Multipart
    @POST("file/image/upload")
    Call<URLBean> getURL(@Part MultipartBody.Part part);
}
