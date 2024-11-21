package edu.northeastern.myapplicationcs5300.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.northeastern.myapplicationcs5300.ClassificationRecord;
import edu.northeastern.myapplicationcs5300.R;
import edu.northeastern.myapplicationcs5300.SharedViewModel;

public class GalleryFragment extends Fragment {

    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        // 初始化 ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // 获取布局中的组件
        TextView summaryTextView = root.findViewById(R.id.text_gallery_summary);
        PieChart userChoicesPieChart = root.findViewById(R.id.user_choices_pie_chart);
        BarChart modelPredictionsBarChart = root.findViewById(R.id.model_predictions_bar_chart);

        // 观察 ViewModel 的数据变化
        sharedViewModel.getClassificationHistory().observe(getViewLifecycleOwner(), history -> {
            double accuracy = calculateAccuracy(history);
            Map<String, Integer> userChoices = calculateUserChoicesDistribution(history);
            Map<String, Integer> modelPredictions = calculateModelPredictionsDistribution(history);

            // 设置总体统计信息
            summaryTextView.setText(String.format("Overall Accuracy: %.2f%%", accuracy));

            // 显示用户选择的饼图
            showPieChart(userChoicesPieChart, userChoices, "User Choices Distribution");

            // 显示模型分类的柱状图
            showBarChart(modelPredictionsBarChart, modelPredictions, "Model Predictions Distribution");
        });

        return root;
    }

    private double calculateAccuracy(List<ClassificationRecord> history) {
        int correctCount = 0;
        for (ClassificationRecord record : history) {
            if (record.isCorrect()) {
                correctCount++;
            }
        }
        return history.isEmpty() ? 0 : (correctCount * 100.0 / history.size());
    }

    private Map<String, Integer> calculateUserChoicesDistribution(List<ClassificationRecord> history) {
        return history.stream()
                .collect(Collectors.groupingBy(record -> record.getUserChoice(), Collectors.summingInt(e -> 1)));
    }

    private Map<String, Integer> calculateModelPredictionsDistribution(List<ClassificationRecord> history) {
        return history.stream()
                .collect(Collectors.groupingBy(record -> record.getModelPrediction(), Collectors.summingInt(e -> 1)));
    }

    private void showPieChart(PieChart pieChart, Map<String, Integer> data, String description) {
        List<PieEntry> entries = new ArrayList<>();
        int total = data.values().stream().mapToInt(Integer::intValue).sum(); // 总数，用于计算百分比
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            float percentage = (entry.getValue() * 100f) / total; // 计算百分比
            entries.add(new PieEntry(percentage, entry.getKey()));
        }
        PieDataSet dataSet = new PieDataSet(entries, description);
        dataSet.setSliceSpace(2f); // 设置切片间距
        dataSet.setValueTextSize(12f); // 设置文本大小
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true); // 显示百分比
        pieChart.getDescription().setEnabled(false); // 隐藏默认描述
        pieChart.setCenterText(description); // 设置中心文本
        pieChart.setEntryLabelTextSize(12f); // 设置标签文本大小
        pieChart.invalidate(); // 刷新图表
    }


    private void showBarChart(BarChart barChart, Map<String, Integer> data, String description) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new BarEntry(index++, entry.getValue())); // Add entry with index
            labels.add(entry.getKey()); // Save label for this index
        }

        BarDataSet dataSet = new BarDataSet(entries, description);
        dataSet.setValueTextSize(12f); // Set text size for values
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Set X-axis labels
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = Math.round(value); // Convert float to nearest integer
                if (index >= 0 && index < labels.size()) {
                    return labels.get(index); // Return the label at the index
                }
                return ""; // Fallback for out-of-bounds
            }
        });
        barChart.getXAxis().setGranularity(1f); // Ensure 1 interval per label
        barChart.getXAxis().setGranularityEnabled(true);

        barChart.getDescription().setEnabled(false); // Hide default description
        barChart.invalidate(); // Refresh the chart
    }


}
