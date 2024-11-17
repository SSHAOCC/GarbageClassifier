package edu.northeastern.myapplicationcs5300;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<ClassificationRecord>> classificationHistory = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<ClassificationRecord>> getClassificationHistory() {
        return classificationHistory;
    }

    public void addClassificationRecord(ClassificationRecord record) {
        List<ClassificationRecord> currentHistory = classificationHistory.getValue();
        if (currentHistory != null) {
            currentHistory.add(record);
            classificationHistory.setValue(currentHistory);
        }
    }
}
