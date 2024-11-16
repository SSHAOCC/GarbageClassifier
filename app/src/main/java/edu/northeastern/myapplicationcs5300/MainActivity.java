package edu.northeastern.myapplicationcs5300;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import edu.northeastern.myapplicationcs5300.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Interpreter tflite; // TensorFlow Lite 模型解释器
    private String[] labels = {"cardboard", "glass", "metal", "paper", "plastic", "trash"}; // 垃圾分类标签

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用 ViewBinding 绑定布局
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置 Toolbar
        setSupportActionBar(binding.appBarMain.toolbar);

        // 加载并应用保存的颜色
        int savedColor = loadColorPreference();
        applyInterfaceColor(savedColor);

        // 初始化 DrawerLayout 和 NavigationView
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 设置 FloatingActionButton 点击事件
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // 示例图像用于测试
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bottle);
                    String result = predictImage(bitmap); // 调用预测方法
                    Snackbar.make(view, "Classification: " + result, Snackbar.LENGTH_LONG)
                            .setAnchorView(binding.appBarMain.fab)
                            .show();
                } catch (Exception e) {
                    Snackbar.make(view, "Error during prediction: " + e.getMessage(), Snackbar.LENGTH_LONG)
                            .setAnchorView(binding.appBarMain.fab)
                            .show();
                }
            }
        });

        // 加载 TensorFlow Lite 模型
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 添加菜单
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // 导航栏返回逻辑
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * 加载 TensorFlow Lite 模型文件
     */
    private MappedByteBuffer loadModelFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(getAssets().openFd("waste_classification_model.tflite").getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = getAssets().openFd("waste_classification_model.tflite").getStartOffset();
        long declaredLength = getAssets().openFd("waste_classification_model.tflite").getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /**
     * 预测图像类别
     */
    private String predictImage(Bitmap bitmap) {
        // 将输入图像调整为 224x224 大小并进行归一化处理
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        float[][][][] input = new float[1][224][224][3];
        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int pixel = resizedBitmap.getPixel(x, y);
                input[0][x][y][0] = (pixel >> 16 & 0xFF) / 255.0f; // Red
                input[0][x][y][1] = (pixel >> 8 & 0xFF) / 255.0f;  // Green
                input[0][x][y][2] = (pixel & 0xFF) / 255.0f;       // Blue
            }
        }

        // 创建输出数组
        float[][] output = new float[1][labels.length];
        tflite.run(input, output);

        // 找出概率最高的分类
        int maxIndex = -1;
        float maxProb = -1;
        for (int i = 0; i < labels.length; i++) {
            if (output[0][i] > maxProb) {
                maxProb = output[0][i];
                maxIndex = i;
            }
        }
        return labels[maxIndex];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tflite != null) {
            tflite.close(); // 释放模型资源
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 获取点击的菜单项 ID
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // 显示颜色选择对话框
            showColorPickerDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showColorPickerDialog() {
        // 定义颜色选项 (可以自定义添加更多颜色)
        final int[] colors = {
                0xFFE57373, // Red
                0xFF81C784, // Green
                0xFF64B5F6, // Blue
                0xFFFFD54F, // Yellow
                0xFFA1887F, // Brown
                0xFF90A4AE  // Gray
        };
        final String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Brown", "Gray"};

        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a color for the interface");
        builder.setItems(colorNames, (dialog, which) -> {
            // 应用选中的颜色
            int selectedColor = colors[which];
            applyInterfaceColor(selectedColor);

            // 保存用户选择的颜色
            saveColorPreference(selectedColor);
        });

        // 显示对话框
        builder.create().show();
    }

    private void applyInterfaceColor(int color) {
        // 修改 Toolbar 的背景颜色
        binding.appBarMain.toolbar.setBackgroundColor(color);
    }

    private void saveColorPreference(int color) {
        getSharedPreferences("app_preferences", MODE_PRIVATE)
                .edit()
                .putInt("interface_color", color)
                .apply();
    }

    private int loadColorPreference() {
        // 默认颜色 (可以是白色或其他颜色)
        return getSharedPreferences("app_preferences", MODE_PRIVATE)
                .getInt("interface_color", 0xFF64B5F6);
    }

}
