package com.example.helloworld.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworld.Bean.RequestBean;
import com.example.helloworld.InternetInterface.RegisertRequestInterface;
import com.example.helloworld.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private Button regist_btn;
    private EditText account_tv,password_tv,password2_tv;

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
        setContentView(R.layout.activity_registertest1);

        regist_btn = findViewById(R.id.button_reg);
        account_tv=findViewById(R.id.registerAccount);
        password_tv=findViewById(R.id.registerPassword);
        password2_tv=findViewById(R.id.registerPassword2);
        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=account_tv.getText().toString();
                String password=password_tv.getText().toString();
                String password2=password2_tv.getText().toString();
//                Toast.makeText(RegisterActivity.this,password+" and "+password2,Toast.LENGTH_SHORT).show();
                if(!password.equals(password2))
                {
//                    Toast.makeText(RegisterActivity.this,password+" and "+password2,Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://49.235.134.191:8080/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RegisertRequestInterface regisertRequestInterface = retrofit.create(RegisertRequestInterface.class);
                    regisertRequestInterface.register(account,password).enqueue(new Callback<RequestBean>() {
                        @Override
                        public void onResponse(Call<RequestBean> call, Response<RequestBean> response) {
                            RequestBean requestBean =response.body();
                            if(requestBean.getCode()==200)
                            {
                                Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestBean> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this,"额，好像出了点错误...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //改状态栏的图标颜色
    public void changeStatusBarTextColor(boolean b){
        if(b) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}
