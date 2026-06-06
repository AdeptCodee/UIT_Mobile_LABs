package com.example.healmeasureapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Khai báo các biến giao diện
    private Chronometer chronometer;
    private Button btnStart, btnPause;
    private TextView textHeartRate, textCalories, textDistance, textLaps;

    // Các biến quản lý trạng thái
    private boolean isRunning = false;
    private long thoiGianTamDung = 0;

    // Các biến lưu trữ chỉ số sức khỏe mô phỏng
    private int heartRate = 80;
    private double calories = 0.0;
    private double distance = 0.0;
    private int laps = 0;

    // Tạo một Handler để chạy vòng lặp cập nhật dữ liệu mỗi giây
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ giao diện
        chronometer = findViewById(R.id.chronometer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        textHeartRate = findViewById(R.id.textHeartRate);
        textCalories = findViewById(R.id.textCalories);
        textDistance = findViewById(R.id.textDistance);
        textLaps = findViewById(R.id.textLaps);

        // Xử lý nút START
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - thoiGianTamDung);
                    chronometer.start();
                    isRunning = true;

                    // Bắt đầu vòng lặp mô phỏng dữ liệu
                    handler.post(updateStatsRunnable);
                }
            }
        });

        // Xử lý nút PAUSE
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    chronometer.stop();
                    thoiGianTamDung = SystemClock.elapsedRealtime() - chronometer.getBase();
                    isRunning = false;

                    // Dừng vòng lặp cập nhật dữ liệu
                    handler.removeCallbacks(updateStatsRunnable);
                }
            }
        });
    }

    // Đây là "Khối động cơ" chạy ngầm mỗi 1 giây (1000 mili-giây)
    private Runnable updateStatsRunnable = new Runnable() {
        @Override
        public void run() {
            // 1. Tạo dữ liệu giả
            heartRate = 90 + (int)(Math.random() * 30); // Nhịp tim nhảy ngẫu nhiên từ 90 đến 119
            calories += 0.12; // Mỗi giây đốt 0.12 calo
            distance += 0.003; // Mỗi giây chạy được 3 mét (0.003 km)

            // Cứ mỗi 0.5 km thì tính là 1 vòng (Lap)
            if (distance >= (laps + 1) * 0.5) {
                laps++;
            }

            // 2. Cập nhật dữ liệu mới lên màn hình
            textHeartRate.setText("Heart Rate: " + heartRate);
            textCalories.setText(String.format("Burn %.1f cal", calories));
            textDistance.setText(String.format("Distance: %.2f km", distance));
            textLaps.setText("Lap: " + laps);

            // 3. Lệnh yêu cầu "Khối động cơ" này tự động lặp lại sau 1000 mili-giây (1 giây)
            handler.postDelayed(this, 1000);
        }
    };
}