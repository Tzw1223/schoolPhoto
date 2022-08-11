package com.example.helloworld.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworld.Bean.EvaluateBean;
import com.example.helloworld.InternetInterface.EvaluateService;
import com.example.helloworld.R;
import com.example.helloworld.config.Constract;
import com.example.helloworld.entity.Evaluate;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EvaluateActivity extends AppCompatActivity {

    Retrofit retrofit;
    Evaluate eva;
    RatingBar result,speed;
    EditText evaluate;
    Button subBtn;
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置状态栏颜色
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        //设置状态栏字体颜色
        changeStatusBarTextColor(true);

        //设置标题栏不可见
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

        Intent intent = getIntent();
        id = intent.getIntExtra("fdId",0);
        eva = new Evaluate();
        result = findViewById(R.id.ratingBar);
        speed = findViewById(R.id.ratingBar2);
        subBtn = findViewById(R.id.subButton);
        evaluate = findViewById(R.id.editTextTextMultiLine);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constract.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EvaluateService evaService=retrofit.create(EvaluateService.class);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eva.setRatingResult((int)result.getRating());
                eva.setRatingSpeed((int)speed.getRating());
                eva.setFeedBackId(id);
                eva.setCommend(evaluate.getText().toString());
                Gson gson = new Gson();
                String json = gson.toJson(eva);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
                Call<EvaluateBean> repos = evaService.postEva(body);
                Runnable r = new Runnable(){

                    @Override
                    public void run() {
                        try {
                            EvaluateBean evaBean = repos.execute().body();
                            runOnUiThread(() -> {
                                if(evaBean.getCode()==200){
                                    Toast.makeText(EvaluateActivity.this,"发布成功！",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(EvaluateActivity.this,"诶呀，数据好像出错了！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(r);
                thread.start();
            }
        });
    }

    /*private Evaluate initEvaluate(long feedbackId){

        e.setRatingResult((int)result.getRating());
        e.setRatingSpeed((int)speed.getRating());
        e.setFeedBackId(feedbackId);
        e.setCommend(evaluate.getText().toString());
    }*/

    public void changeStatusBarTextColor(boolean b){
        if(b) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}
