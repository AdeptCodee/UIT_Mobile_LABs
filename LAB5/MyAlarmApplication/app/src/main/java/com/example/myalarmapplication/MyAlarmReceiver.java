package com.example.myalarmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class MyAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TuHocAndroid", "Đã nhận được tín hiệu từ AlarmManager!");

        // Tạo Intent để gọi Foreground Service
        Intent serviceIntent = new Intent(context, AlarmForegroundService.class);

        // Kích hoạt Foreground Service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}