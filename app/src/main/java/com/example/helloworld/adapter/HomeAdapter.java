package com.example.helloworld.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.helloworld.Activity.MainActivity;
import com.example.helloworld.Activity.loginActivity;
import com.example.helloworld.Bean.NewsBean;
import com.example.helloworld.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
    //新闻信息
//    private String[] titles={"防控疫情，带好口罩","食堂门口漏油！","爱护草坪，带走垃圾","井盖碎裂","标题标题"};
//    private int[] icons={R.drawable.finish_result,R.drawable.news_test1,R.drawable.news_test2,R.drawable.news_test3,R.drawable.news_test5};
//    private String[] contents={
//            "近日发现有些同学没戴口罩直接进入食堂，疫情尚未结束，请大家进入食堂前自觉戴好口罩",
//            "紫荆食堂门口的地面上都是油，好几个同学骑电动车经过滑倒，大家小心！注意绕行！",
//            "昨天，有同学在阳光园的草地上野餐没有及时清理垃圾，请同学们自觉带走垃圾，共同维护文明校园",
//            "生活三区到教学区的红绿灯路口处井盖碎裂，目前已在维修，大家小心，注意绕行",
//            "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容"
//    };
//    private String[] dates={"2021-05-24 12:35:20","2021-10-12 16:34:13","2021-10-13 17:30:12","2021-10-13 18:00:02","xxxx-xx-xx xx:xx:xx"};
    private Context context;
    private List<NewsBean.Data>news=null;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public HomeAdapter(Context context, List<NewsBean.Data> news){
        this.context=context;
        this.news=news;
    }

    public void refresh(List<NewsBean.Data> list) {
//        this.news.clear();
        for(int i=0;i<list.size();i++) {
            this.news.set(i,list.get(i));
        }
        notifyItemRangeChanged(0,list.size());
    }

    @NonNull
    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item1,parent,false);
        HomeAdapter.MyViewHolder myViewHolder=new HomeAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.MyViewHolder holder, int position) {
        NewsBean.Data data=news.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.account.setText(data.getPublishAccount());
        holder.title.setText(data.getTitle());
        holder.content.setText(data.getDesc());
        holder.date.setText(sdf.format(data.getPublishTime()));
        holder.account.setText(data.getPublishAccount());
        Glide.with(context).load(data.getImageUrl()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        TextView date;
        TextView account;
        ImageView iv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
            date=itemView.findViewById(R.id.date);
            account=itemView.findViewById(R.id.account);
            iv=itemView.findViewById(R.id.iv);
        }
    }
}