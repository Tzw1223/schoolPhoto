package com.example.helloworld.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.Bean.NewsBean;
import com.example.helloworld.InternetInterface.NewsRequestInterface;
//import com.example.helloworld.Manager.OkHttpManager;
import com.example.helloworld.R;
import com.example.helloworld.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import com.example.helloworld.adapter.HomeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private List<NewsBean.Data> initNews = new ArrayList<NewsBean.Data>();
    private HomeAdapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textView;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //列表recyclerview初始化
        View mView = inflater.inflate(R.layout.fragment_home, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.235.134.191:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NewsRequestInterface newsRequestInterface = retrofit.create(NewsRequestInterface.class);
        newsRequestInterface.getNews().enqueue(new Callback<NewsBean>() {
            @Override
            public void onResponse(Call<NewsBean> call, Response<NewsBean> response) {
                NewsBean newsBean=response.body();
                if(newsBean.getCode()==200){
//                    initNews.clear();
                    List<NewsBean.Data> list=newsBean.getData();
                    RecyclerView recyclerView=mView.findViewById(R.id.newslist);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    homeAdapter=new HomeAdapter(getContext(),list);
                    recyclerView.setAdapter(homeAdapter);
                }
                else {
                    Toast.makeText(getActivity(),"获取新闻列表失败",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NewsBean> call, Throwable t) {

            }
        });


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://49.235.134.191:8080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        NewsRequestInterface newsRequestInterface = retrofit.create(NewsRequestInterface.class);
//        newsRequestInterface.getNews().enqueue(new Callback<NewsBean>() {
//            @Override
//            public void onResponse(Call<NewsBean> call, Response<NewsBean> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<NewsBean> call, Throwable t) {
//
//            }
//        });
        return mView;
        //return root;
    }

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}