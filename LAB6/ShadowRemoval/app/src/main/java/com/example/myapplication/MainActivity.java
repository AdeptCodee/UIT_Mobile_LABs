package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

// Thư viện kiểm tra OpenCV
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnLoad, btnRemoveShadow;
    ImageView imageView;
    Bitmap currentBitmap; // Dùng để lưu trữ bức ảnh bạn chọn

    // Bộ công cụ đợi bạn chọn ảnh từ thư viện điện thoại trả về
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        // Nhận ảnh và in lên màn hình
                        currentBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageView.setImageBitmap(currentBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BẮT BUỘC: Kiểm tra OpenCV có kết nối được không
        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV_Setup", "Thư viện OpenCV đã được kết nối!");
        } else {
            Toast.makeText(this, "Lỗi: Không thể kết nối thư viện OpenCV", Toast.LENGTH_LONG).show();
        }

        btnLoad = findViewById(R.id.btnLoad);
        btnRemoveShadow = findViewById(R.id.btnRemoveShadow);
        imageView = findViewById(R.id.imageView);

        // Nút 1: Mở thư viện chọn ảnh
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagePickerLauncher.launch(intent);
            }
        });

        // Nút 2: Xử lý xóa bóng
        btnRemoveShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBitmap != null) {
                    Toast.makeText(MainActivity.this, "Đang lọc bóng đổ, vui lòng đợi...", Toast.LENGTH_SHORT).show();

                    // Gọi file ShadowRemovalFilter để xử lý
                    ShadowRemovalFilter.getShadowFilteredImage(currentBitmap, new ShadowRemovalFilter.MyCallBack() {
                        @Override
                        public void onComplete(Bitmap processedBitmap) {
                            // Nhận ảnh sạch và cập nhật lên màn hình
                            imageView.setImageBitmap(processedBitmap);
                            currentBitmap = processedBitmap;
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Bạn chưa chọn bức ảnh nào!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}