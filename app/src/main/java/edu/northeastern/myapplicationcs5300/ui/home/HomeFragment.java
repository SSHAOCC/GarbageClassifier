package edu.northeastern.myapplicationcs5300.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.northeastern.myapplicationcs5300.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 使用 ViewBinding 加载布局
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 设置标题
        binding.textHomeTitle.setText("Welcome to the Waste Classification App!");

        // 设置主要描述
        binding.textHomeDescription.setText(
                "Our app helps you classify waste into six categories. Tap the camera button at the bottom right to get started! " +
                        "Whether you choose an image from the gallery or take a new one, we’ll help you identify the best match. " +
                        "Don’t forget: we’ll test if your guess matches the model’s!"
        );

        // 设置设置说明
        binding.textHomeSettings.setText(
                "In the top right corner, you can access 'Settings' to view your usage history, including overall consistency statistics " +
                        "between your guesses and the model predictions."
        );

        // 设置侧边栏说明
        binding.textHomeSidebar.setText(
                "Use the sidebar in the top left to navigate between 'Home,' 'Statistics,' and 'About.' The 'Statistics' page provides " +
                        "detailed visualizations of your history from different perspectives."
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
