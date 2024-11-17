package edu.northeastern.myapplicationcs5300;

public class ClassificationRecord {
    private final String modelPrediction; // 模型预测结果
    private final String userChoice;      // 用户选择
    private final boolean isCorrect;      // 是否正确

    public ClassificationRecord(String modelPrediction, String userChoice, boolean isCorrect) {
        this.modelPrediction = modelPrediction;
        this.userChoice = userChoice;
        this.isCorrect = isCorrect;
    }

    // 返回模型的预测结果
    public String getModelPrediction() {
        return modelPrediction;
    }

    // 返回用户的选择
    public String getUserChoice() {
        return userChoice;
    }

    // 返回是否正确
    public boolean isCorrect() {
        return isCorrect;
    }

    @Override
    public String toString() {
        return "Model: " + modelPrediction + ", User: " + userChoice + ", Correct: " + isCorrect;
    }
}
