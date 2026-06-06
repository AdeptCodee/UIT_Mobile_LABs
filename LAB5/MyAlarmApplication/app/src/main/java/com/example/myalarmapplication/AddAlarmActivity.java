package com.example.myalarmapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
    EditText etLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        timePicker = findViewById(R.id.timePicker);
        btnSaveAlarm = findViewById(R.id.btnSaveAlarm);
        etLabel = findViewById(R.id.etLabel);

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

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                
                // KIỂM TRA QUYỀN BÁO THỨC CHÍNH XÁC (Cho Android 12+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                        Intent intentPerm = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        startActivity(intentPerm);
                        Toast.makeText(AddAlarmActivity.this, "Vui lòng cấp quyền báo thức chính xác!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                Intent intent = new Intent(AddAlarmActivity.this, MyAlarmReceiver.class);
                
                // Dùng requestCode duy nhất để không bị ghi đè báo thức
                int requestCode = (int) System.currentTimeMillis();
                
                int flags = PendingIntent.FLAG_UPDATE_CURRENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    flags |= PendingIntent.FLAG_IMMUTABLE;
                }
                
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AddAlarmActivity.this, requestCode, intent, flags
                );

                if (alarmManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                    Toast.makeText(AddAlarmActivity.this, "Đã cài báo thức lúc " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
                }

                // Gửi dữ liệu về MainActivity để hiển thị lên màn hình
                Intent resultIntent = new Intent();
                resultIntent.putExtra("hour", hour);
                resultIntent.putExtra("minute", minute);
                resultIntent.putExtra("label", (etLabel != null && !etLabel.getText().toString().isEmpty()) ? etLabel.getText().toString() : "A New Alarm!");

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}