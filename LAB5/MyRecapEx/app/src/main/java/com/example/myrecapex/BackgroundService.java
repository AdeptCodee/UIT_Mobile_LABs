package com.example.myrecapex;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {

    // Hàm này chạy khi Service được gọi bởi Activity
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Tạo một luồng (Thread) chạy ngầm để ứng dụng không bị đơ
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    Log.d("Lab5", "Background task đang đếm số: " + i);
                    try {
                        Thread.sleep(1000); // Tạm dừng 1 giây để giả lập tác vụ tốn thời gian
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Tự động tắt Service sau khi đếm xong để giải phóng bộ nhớ
                stopSelf();
            }
        }).start();

        return START_NOT_STICKY;
    }

    // Vì đây là Background Service, chúng ta trả về null (không cho phép Binding trực tiếp)
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}