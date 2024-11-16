package edu.northeastern.myapplicationcs5300;

class ClassificationRecord {
    String modelPrediction; // 模型预测结果
    String userChoice;      // 用户选择
    boolean isCorrect;      // 是否匹配

    public ClassificationRecord(String modelPrediction, String userChoice, boolean isCorrect) {
        this.modelPrediction = modelPrediction;
        this.userChoice = userChoice;
        this.isCorrect = isCorrect;
    }

    @Override
    public String toString() {
        return "Model: " + modelPrediction + ", User: " + userChoice + ", Correct: " + isCorrect;
    }
}
