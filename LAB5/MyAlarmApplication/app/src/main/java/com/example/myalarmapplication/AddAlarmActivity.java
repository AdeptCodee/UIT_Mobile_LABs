package com.example.myalarmapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity {

    TimePicker timePicker;
    Button btnSaveAlarm;
    EditText etLabel; // Thêm biến để lấy nhãn báo thức

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        timePicker = findViewById(R.id.timePicker);
        btnSaveAlarm = findViewById(R.id.btnSaveAlarm);
        etLabel = findViewById(R.id.etLabel); // Ánh xạ EditText từ giao diện

        btnSaveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;

                if (Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }

                // Cài đặt AlarmManager (Giữ nguyên code chuẩn của bạn)
                Intent intent = new Intent(AddAlarmActivity.this, MyAlarmReceiver.class);
                int flags = PendingIntent.FLAG_UPDATE_CURRENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    flags |= PendingIntent.FLAG_IMMUTABLE;
                }
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AddAlarmActivity.this, 0, intent, flags
                );

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                    Toast.makeText(AddAlarmActivity.this, "Đã cài báo thức thành công lúc " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
                }

                // PHẦN MỚI: Đóng gói dữ liệu gửi về MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("hour", hour);
                resultIntent.putExtra("minute", minute);
                // Tránh lỗi rỗng nếu chưa có etLabel trong XML
                if (etLabel != null) {
                    resultIntent.putExtra("label", etLabel.getText().toString());
                } else {
                    resultIntent.putExtra("label", "Báo thức mới");
                }

                // Xác nhận kết quả và đóng màn hình AddAlarmActivity
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}