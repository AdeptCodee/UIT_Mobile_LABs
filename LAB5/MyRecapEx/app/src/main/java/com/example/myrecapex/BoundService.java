package com.example.myrecapex;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BoundService extends Service {

    // 1. Tạo một đối tượng Binder để chia sẻ tiến trình này với MainActivity [cite: 137]
    private final IBinder binder = new LocalBinder();
    private int count = 0;
    private boolean isRunning = true;

    public class LocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 2. Chạy một luồng ngầm để tự động tăng số đếm lên mỗi giây
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(1000);
                        count++;
                        Log.d("Lab5", "BoundService đang đếm ngầm: " + count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // 3. Hàm này bắt buộc phải trả về binder khi có Activity kết nối tới [cite: 151, 153]
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // 4. Đây là hàm công khai (public) để MainActivity có thể gọi và lấy số đếm
    public int getCurrentCount() {
        return count;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false; // Dừng vòng lặp đếm khi service hủy
    }
}