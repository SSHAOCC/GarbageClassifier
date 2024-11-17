package edu.northeastern.myapplicationcs5300.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.northeastern.myapplicationcs5300.R;

public class SlideshowFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        // 设置标题和详细信息
        TextView titleTextView = root.findViewById(R.id.text_about_title);
        TextView detailsTextView = root.findViewById(R.id.text_about_details);

        titleTextView.setText("About");
        detailsTextView.setText("Developers: Haochen Su, Jiexian Li, Yue Zhang\n" +
                "Organization: Northeastern University\n" +
                "Purpose: This app is designed for Course CS5330 (2024 Fall) Final Project: waste classification.\n" +
                "Copyright: © 2024 Northeastern University.");

        return root;
    }
}
