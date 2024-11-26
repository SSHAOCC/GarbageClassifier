# Automated Waste Sorting Assistant

An **automated waste sorting system** that classifies waste into six categories: cardboard, glass, metal, paper, plastic, and trash. The system leverages the lightweight and efficient **MobileNetV2** model and achieves a **test accuracy of 94.82%**. Optimized for mobile deployment using **TensorFlow Lite**, the system is designed for real-time applications.

---

## Features

- **High Accuracy**: Classifies six waste categories with a test accuracy of 94.82%.
- **Lightweight Architecture**: Uses MobileNetV2 for mobile and resource-constrained environments.
- **Mobile-Ready**: Optimized for deployment on Android devices using TensorFlow Lite.
- **Real-Time Classification**: Enables quick and efficient waste sorting.

---

## How to Train the Model

### 1. Setup Training Environment

- Use **Google Colab** or **Kaggle Notebook**.
- Ensure all necessary dependencies (e.g., TensorFlow) are installed.

### 2. Train the Model

1. Navigate to the `train_model` folder in the repository.
2. Upload the dataset and script to your preferred cloud platform.
3. Adjust dataset paths in the script if necessary.
4. Train the model.
5. Export the trained model as a `.tflite` file.

### 3. Output

- Download the `.tflite` file for deployment.

---

## How to Deploy on Android

### 1. Add TensorFlow Lite Dependencies

In the `build.gradle` file of your Android project, add the following dependencies:

```gradle
implementation 'org.tensorflow:tensorflow-lite:2.12.0'
implementation 'org.tensorflow:tensorflow-lite-support:0.4.3'
```
### 2. Place the Model in Assets

Copy the `waste_classification_model.tflite` file to the `assets` directory:

```plaintext
app/
 ├── src/
 │   ├── main/
 │       ├── assets/
 │           ├── waste_classification_model.tflite
Enable support for .tflite files in the build.gradle file:
android {
    aaptOptions {
        noCompress "tflite"
    }
}
```

---

### 3. Load and Run the Model

Use TensorFlow Lite's API in your Android project to load and run the model for waste classification. Pass image data to the model and retrieve the predicted waste category.

---

## Future Improvements

```
- **Dataset Expansion**: Add more samples for underrepresented categories to improve classification accuracy.
- **Fine-Tuning**: Unfreeze MobileNetV2 layers for better domain-specific feature extraction.
- **Real-World Deployment**: Integrate into mobile apps and industrial robotic systems for scalable waste sorting.
```

---

## Contributing

```
Contributions are welcome! Feel free to open issues or submit pull requests for improvements.
```

---

## License

```
This project is licensed under the MIT License. See the `LICENSE` file for details.
```

