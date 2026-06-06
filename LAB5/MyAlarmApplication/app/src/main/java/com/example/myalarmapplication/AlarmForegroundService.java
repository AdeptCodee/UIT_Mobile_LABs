package com.example.myalarmapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

public class AlarmForegroundService extends Service {

    private static final String CHANNEL_ID = "AlarmChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 1. Tạo Kênh thông báo
        createNotificationChannel();

        // 2. Tạo thông báo neo trên màn hình
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Báo thức đang kêu!")
                .setContentText("Dậy thôi Nguyên ơi...")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .build();

        // 3. Khởi chạy Foreground Service (áp dụng code chống lỗi Android 14 như bạn đã học)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(2, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(2, notification);
        }

        // 4. Giả lập tiếng chuông bằng cách hiện Toast 5 lần
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    final int count = i;
                    // Đẩy Toast lên giao diện chính
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlarmForegroundService.this, "Reng reng reng! Lần " + count, Toast.LENGTH_SHORT).show();
                        }
                    });

                    try {
                        Thread.sleep(2000); // Đợi 2 giây rồi hiện Toast tiếp theo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 5. Sau khi "kêu" xong 5 lần thì tự động tắt Service
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_REMOVE);
                } else {
                    stopForeground(true);
                }
                stopSelf();
            }
        }).start();

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Kênh Báo Thức",
                    NotificationManager.IMPORTANCE_HIGH // Đặt độ ưu tiên cao
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}