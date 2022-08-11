package com.example.helloworld.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.Bean.FeedbackBean;
import com.example.helloworld.InternetInterface.FeedbackRequestInterface;
import com.example.helloworld.R;
import com.example.helloworld.config.Constract;

import java.util.List;

import com.example.helloworld.adapter.HistoryAdapter;
import com.example.helloworld.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        setContentView(R.layout.activity_history);
        initView();
    }

    private void initView() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.235.134.191:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FeedbackRequestInterface feedbackRequestInterface = retrofit.create(FeedbackRequestInterface.class);

        feedbackRequestInterface.getFeedback(User.user.getAccount()).enqueue(new Callback<FeedbackBean>() {
            @Override
            public void onResponse(Call<FeedbackBean> call, Response<FeedbackBean> response) {
                FeedbackBean feedbackBean=response.body();
                if(feedbackBean.getCode()==Constract.REQUESTSUCCESS) {
                    List<FeedbackBean.Data> list=feedbackBean.getData();
                    RecyclerView recyclerView=findViewById(R.id.historylist);
                    LinearLayoutManager manager = new LinearLayoutManager(HistoryActivity.this);//创建线性布局管理器
                    manager.setOrientation(LinearLayoutManager.VERTICAL);//添加垂直布局
                    recyclerView.setLayoutManager(manager);
                    historyAdapter=new HistoryAdapter(HistoryActivity.this,list);
                    historyAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener(){

                        @Override
                        public void onButtonClicked(View view, int position, int i, int id) {
                            if(i==1){
                                Intent intent = new Intent(HistoryActivity.this,TraceActivity.class);
                                intent.putExtra("fdId",id);
                                startActivity(intent);
                            }
                            else if(i==2){
                                Intent intent = new Intent(HistoryActivity.this,EvaluateActivity.class);
                                intent.putExtra("fdId",id);
                                startActivity(intent);
                            }
                        }

                    });
                    recyclerView.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onFailure(Call<FeedbackBean> call, Throwable t) {

            }
        });
    }

    private void changeStatusBarTextColor(boolean b) {
        if(b) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}
