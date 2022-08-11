package com.example.helloworld.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworld.Bean.RequestBean;
import com.example.helloworld.InternetInterface.LoginRequestInterface;
import com.example.helloworld.R;
import com.example.helloworld.config.Constract;
import com.example.helloworld.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView regist_btn;
    private Button log_btn;
    private EditText userName_et;
    private EditText password_et;
    private CheckBox rememberPsw_cb;
    private CheckBox autoLogin_cb;
    private SharedPreferences sp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logintest1);

        userName_et = findViewById(R.id.editTextTextAccount);
        password_et = findViewById(R.id.editTextTextPassword);
        rememberPsw_cb = findViewById(R.id.checkbox_rememberPsw);
        autoLogin_cb = findViewById(R.id.checkbox_autoLogin);
        log_btn = findViewById(R.id.button_login);
        log_btn.setOnClickListener(this);
        regist_btn = findViewById(R.id.textView3);
        regist_btn.setOnClickListener(this);
        sp = this.getSharedPreferences(Constract.SPNAME,MODE_PRIVATE);
        if(sp.getBoolean(Constract.ISREMEMBER,false)) {
            userName_et.setText(sp.getString(Constract.ACCOUNT,""));
            password_et.setText(sp.getString(Constract.PASSWORD,""));
            rememberPsw_cb.setChecked(true);
        }
        if(sp.getBoolean(Constract.ISAUTO,false)){
            userName_et.setText(sp.getString(Constract.ACCOUNT,""));
            password_et.setText(sp.getString(Constract.PASSWORD,""));
            autoLogin_cb.setChecked(true);
            autoLogin();
        }
//        User user = SPSave.getUserInfo(this);
//        User user = SQLiteHelper.getUserInfo(this);
//        if(user.isRemberPassword())
//        {
//            userName_et.setText(user.getUserName());
//            password_et.setText(user.getPassword());
//            rememberPsw_cb.setChecked(true);
//        }
//        if(user.isAutoLogin())
//        {
//            autoLogin_cb.setChecked(true);
//            autoLogin();
//        }
    }

    public boolean saveUserInfo(){
        SharedPreferences.Editor editor = sp.edit();//获取Editor
        editor.putString(Constract.ACCOUNT,userName_et.getText().toString());
        editor.putString(Constract.PASSWORD,password_et.getText().toString());
        editor.putBoolean(Constract.ISREMEMBER,rememberPsw_cb.isChecked());
        editor.putBoolean(Constract.ISAUTO,autoLogin_cb.isChecked());
        return editor.commit();
    }

    //改状态栏的图标颜色
    public void changeStatusBarTextColor(boolean b){
        if(b) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    private boolean autoLogin()//自动登录
    {
        //SP存用户名密码
//        User user = SPSave.getUserInfo(this);
        // SQLite存用户名密码
//        User user = SQLiteHelper.getUserInfo(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constract.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginRequestInterface loginRequestInterface = retrofit.create(LoginRequestInterface.class);
        loginRequestInterface.login(userName_et.getText().toString(),password_et.getText().toString()).enqueue(new Callback<RequestBean>() {
            @Override
            public void onResponse(Call<RequestBean> call, Response<RequestBean> response) {
                RequestBean requestBean = response.body();
                if(requestBean.getCode()==200)
                {
                    Intent intent = new Intent(loginActivity.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(loginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    saveUserInfo();
                    User.setUser(userName_et.getText().toString(),password_et.getText().toString());
                }
                else
                {
                    Toast.makeText(loginActivity.this,"账号或密码已过期，请重新输入！",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RequestBean> call, Throwable t) {
                Toast.makeText(loginActivity.this,"额，好像出了点错误...",Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_login:
                String userName = userName_et.getText().toString();
                String password = password_et.getText().toString();
                if(TextUtils.isEmpty(userName))
                {
                    Toast.makeText(loginActivity.this,"请输入账号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(loginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constract.BASEURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                LoginRequestInterface loginRequestInterface = retrofit.create(LoginRequestInterface.class);
                loginRequestInterface.login(userName,password).enqueue(new Callback<RequestBean>() {
                    @Override
                    public void onResponse(Call<RequestBean> call, Response<RequestBean> response) {
                        RequestBean requestBean = response.body();
                        if(requestBean.getCode()==200)
                        {
                            Intent intent = new Intent(loginActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(loginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            User.setUser(userName_et.getText().toString(),password_et.getText().toString());
//                            if(rememberPsw_cb.isChecked())
//                            {
////                        if(SPSave.saveUserInfo(this,userName,password,rememberPsw_cb.isChecked(),autoLogin_cb.isChecked()))
//                                if(SQLiteHelper.insert(loginActivity.this,userName,password,rememberPsw_cb.isChecked(),autoLogin_cb.isChecked()))
//                                    Toast.makeText(loginActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
//                                else
//                                    Toast.makeText(loginActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
//                            }
                            if(rememberPsw_cb.isChecked()||autoLogin_cb.isChecked()){
                                if(saveUserInfo()){
                                    Toast.makeText(loginActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(loginActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(loginActivity.this,"账号或密码错误！",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<RequestBean> call, Throwable t) {
                        Toast.makeText(loginActivity.this,"额，好像出了点错误...",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.textView3:
                Intent intent = new Intent(loginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
