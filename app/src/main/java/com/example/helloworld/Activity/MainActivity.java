package com.example.helloworld.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.R;
import com.example.helloworld.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
  /*  //新闻信息
    private String[] titles={"防控疫情，带好口罩","食堂门口漏油！","爱护草坪，带走垃圾","井盖碎裂","标题标题"};
    private int[] icons={R.drawable.finish_result,R.drawable.news_test1,R.drawable.news_test2,R.drawable.news_test3,R.drawable.news_test5};
    private String[] contents={
            "近日发现有些同学没戴口罩直接进入食堂，疫情尚未结束，请大家进入食堂前自觉戴好口罩",
            "紫荆食堂门口的地面上都是油，好几个同学骑电动车经过滑倒，大家小心！注意绕行！",
            "昨天，有同学在阳光园的草地上野餐没有及时清理垃圾，请同学们自觉带走垃圾，共同维护文明校园",
            "生活三区到教学区的红绿灯路口处井盖碎裂，目前已在维修，大家小心，注意绕行",
            "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容"
    };
    private String[] dates={"2021-05-24 12:35:20","2021-10-12 16:34:13","2021-10-13 17:30:12","2021-10-13 18:00:02","xxxx-xx-xx xx:xx:xx"};
*/
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

        //初始化页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoolnews);
        //20211013 bug:我在页面切换出现了问题，要查看其它页面布局请直接更改上面，并把非该页面处理的事件等注释掉......

       /* //列表recyclerview初始化
        RecyclerView recyclerView=findViewById(R.id.newslist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter homeAdapter=new HomeAdapter();
        recyclerView.setAdapter(homeAdapter);*/

        //导航
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //设置底部导航栏不会遮挡布局
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


    }

  /*  public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_item1,parent,false);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.title.setText(titles[position]);
            holder.content.setText(contents[position]);
            holder.date.setText(dates[position]);
            holder.iv.setImageResource(icons[position]);
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView content;
            TextView date;
            ImageView iv;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                content=itemView.findViewById(R.id.content);
                date=itemView.findViewById(R.id.date);
                iv=itemView.findViewById(R.id.iv);
            }
        }
    }*/

    //改状态栏的图标颜色
    public void changeStatusBarTextColor(boolean b){
        if(b) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}