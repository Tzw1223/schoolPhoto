package com.example.helloworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helloworld.Bean.FeedbackBean;
import com.example.helloworld.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
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
    private List<FeedbackBean.Data> feedbackList;
    private int id;
    public HistoryAdapter(Context context,List<FeedbackBean.Data> feedbackList){
        this.context=context;
        this.feedbackList=feedbackList;
    }
    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item2,parent,false);
        HistoryAdapter.MyViewHolder myViewHolder=new HistoryAdapter.MyViewHolder(view,mOnItemClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FeedbackBean.Data data = feedbackList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.title.setText(data.getTitle());
        holder.content.setText(data.getDesc());
        holder.date.setText(sdf.format(data.getTime()));
        holder.type.setText(data.getCategory());
        holder.status.setText(data.getProcess());
        Glide.with(context).load(data.getImageUrl()).into(holder.iv);
        id = data.getId();
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onButtonClicked(View view, int position,int i,int id);
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        TextView date;
        TextView address;
        ImageView iv;
        TextView type;
        TextView status;
        Button progressBtn;
        Button evaluateBtn;
        public MyViewHolder(@NonNull View itemView, final OnItemClickListener onClickListener) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
            date=itemView.findViewById(R.id.date);
            iv=itemView.findViewById(R.id.iv);
            type = itemView.findViewById(R.id.type);
            status = itemView.findViewById(R.id.status);
            progressBtn=itemView.findViewById(R.id.progressButton);
            evaluateBtn=itemView.findViewById(R.id.evaluateButton);

            progressBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            onClickListener.onButtonClicked(itemView,position,1,id);
                        }
                    }
                }
            });
            evaluateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            onClickListener.onButtonClicked(itemView,position,2,id);
                        }
                    }
                }
            });
        }
    }
}
