package com.example.helloworld.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.helloworld.Activity.HistoryActivity;
import com.example.helloworld.Activity.PhotoActivity;
import com.example.helloworld.R;
import com.example.helloworld.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private Button pho_btn,his_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        pho_btn = root.findViewById(R.id.button_photo2);
        pho_btn.setOnClickListener(new ButtonListener());
        his_btn=root.findViewById(R.id.button_history);
        his_btn.setOnClickListener(new ButtonListener());
        return root;
    }

    private class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_photo2:
                    Intent intent = new Intent(getActivity(),PhotoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_history:
                    Intent intentHis = new Intent(getActivity(), HistoryActivity.class);
                    startActivity(intentHis);
                    break;
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}