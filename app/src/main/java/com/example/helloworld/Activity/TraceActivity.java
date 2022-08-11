package com.example.helloworld.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworld.Bean.TraceBean;
import com.example.helloworld.InternetInterface.ApiService;
import com.example.helloworld.R;
import com.example.helloworld.adapter.TraceListAdapter;
import com.example.helloworld.config.Constract;
import com.example.helloworld.entity.Trace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TraceActivity extends AppCompatActivity {

    private ListView lvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    Retrofit retrofit;
    private String desc;
    private Date time;
    private int fdBackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            //getSupportActionBar().hide();
            getSupportActionBar().setTitle("查看进度");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        Intent intent = getIntent();
        fdBackId = intent.getIntExtra("fdId",0);
        findView();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constract.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        if(fdBackId!=0) {
            apiService.trace(fdBackId).enqueue(new Callback<TraceBean>() {
                @Override
                public void onResponse(Call<TraceBean> call, Response<TraceBean> response) {
                    TraceBean traceBean = response.body();
                    List<TraceBean.Data> list = traceBean.getData();
                    initData(list);
                }

                @Override
                public void onFailure(Call<TraceBean> call, Throwable t) {

                }
            });
        }
        else{
            Toast.makeText(TraceActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void findView() {
        lvTrace = (ListView) findViewById(R.id.lvTrace);
    }

    private void initData(List<TraceBean.Data> list) {
        for(TraceBean.Data d: list){
            desc = d.getDesc();
            time = d.getTime();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str=sdf.format(time);
            traceList.add(new Trace(str, desc));
        }

        /*// 一些假的数据//在这进行后端数据的读入判断等
        traceList.add(new Trace("2021-12-15 17:48:00", "状态1"));
        traceList.add(new Trace("2021-12-15 14:13:00", "状态2"));
        traceList.add(new Trace("2021-12-15 13:01:04", "状态3"));
*/
        adapter = new TraceListAdapter(this, traceList);
        lvTrace.setAdapter(adapter);
    }
    //改状态栏的图标颜色
    public void changeStatusBarTextColor(boolean b){
        if(b) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}