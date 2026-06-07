package com.example.myalarmapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAddAlarm;
    LinearLayout alarmContainer;

    private final ActivityResultLauncher<Intent> addAlarmLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    int hour = data.getIntExtra("hour", 0);
                    int minute = data.getIntExtra("minute", 0);
                    String label = data.getStringExtra("label");

                    // Vẽ báo thức mới lên màn hình với giao diện đẹp
                    addNewAlarmToUI(hour, minute, label);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddAlarm = findViewById(R.id.fabAddAlarm);
        alarmContainer = findViewById(R.id.alarmContainer);

        // 1. Xin quyền thông báo (Bắt buộc cho Android 13+ để hiện Toast/Notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        fabAddAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
            addAlarmLauncher.launch(intent);
        });
    }

    private void addNewAlarmToUI(int hour, int minute, String label) {
        // 2. Sử dụng LayoutInflater để nạp layout item_alarm.xml thay vì tạo TextView thủ công
        View alarmView = LayoutInflater.from(this).inflate(R.layout.item_alarm, alarmContainer, false);

        TextView tvTime = alarmView.findViewById(R.id.tvAlarmTime);
        TextView tvLabel = alarmView.findViewById(R.id.tvAlarmLabel);

        String amPm = (hour < 12) ? "AM" : "PM";
        int displayHour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        String timeStr = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm);

        tvTime.setText(timeStr);
        tvLabel.setText(label != null && !label.isEmpty() ? label : "A New Alarm!");

        // Thêm báo thức vào đầu danh sách
        if (alarmContainer != null) {
            alarmContainer.addView(alarmView, 0);
        }
    }
}