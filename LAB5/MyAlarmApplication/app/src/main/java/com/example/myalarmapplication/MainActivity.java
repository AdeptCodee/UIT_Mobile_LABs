package com.example.myalarmapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAddAlarm;
    LinearLayout alarmContainer; // Biến chứa danh sách báo thức

    // Khởi tạo bộ đón kết quả trả về từ AddAlarmActivity
    private final ActivityResultLauncher<Intent> addAlarmLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    int hour = data.getIntExtra("hour", 0);
                    int minute = data.getIntExtra("minute", 0);
                    String label = data.getStringExtra("label");

                    // Vẽ báo thức mới lên màn hình
                    addNewAlarmToUI(hour, minute, label);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddAlarm = findViewById(R.id.fabAddAlarm);
        // Ánh xạ LinearLayout từ file giao diện activity_main.xml
        alarmContainer = findViewById(R.id.alarmContainer);

        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                // Dùng launcher thay cho startActivity
                addAlarmLauncher.launch(intent);
            }
        });
    }

    // Hàm phụ trợ giúp tạo ra một TextView hiển thị báo thức mới
    private void addNewAlarmToUI(int hour, int minute, String label) {
        String amPm = (hour < 12) ? "AM" : "PM";
        int displayHour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        String timeStr = String.format("%02d:%02d", displayHour, minute);

        TextView newAlarmView = new TextView(this);
        newAlarmView.setText(timeStr + " " + amPm + " - " + label);
        newAlarmView.setTextSize(24);
        newAlarmView.setPadding(30, 30, 30, 30);
        newAlarmView.setTextColor(getResources().getColor(android.R.color.black));

        // Thêm TextView vừa tạo vào LinearLayout (vị trí trên cùng)
        if (alarmContainer != null) {
            alarmContainer.addView(newAlarmView, 0);
        }
    }
}