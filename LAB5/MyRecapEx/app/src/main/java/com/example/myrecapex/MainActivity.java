package com.example.myrecapex;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button btnBackground, btnForeground, btnBound;
    Button btnAsyncTask, btnModernAsync;
    TextView tvResult;

    BoundService mService;
    boolean isBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            mService = binder.getService();
            isBound = true;
            Toast.makeText(MainActivity.this, "Đã kết nối Bound Service!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBackground = findViewById(R.id.btnBackground);
        btnForeground = findViewById(R.id.btnForeground);
        btnBound = findViewById(R.id.btnBound);
        btnAsyncTask = findViewById(R.id.btnAsyncTask);
        btnModernAsync = findViewById(R.id.btnModernAsync);
        tvResult = findViewById(R.id.tvResult);

        btnBackground.setOnClickListener(v -> startService(new Intent(this, BackgroundService.class)));

        btnForeground.setOnClickListener(v -> {
            checkNotificationPermissionAndStartService();
        });

        btnBound.setOnClickListener(v -> {
            if (isBound) {
                tvResult.setText("Bound Service đếm: " + mService.getCurrentCount());
            } else {
                bindService(new Intent(this, BoundService.class), connection, Context.BIND_AUTO_CREATE);
            }
        });

        btnAsyncTask.setOnClickListener(v -> new MyTask().execute());

        btnModernAsync.setOnClickListener(v -> runModernAsyncTask());
    }

    private void checkNotificationPermissionAndStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            } else {
                startMyForegroundService();
            }
        } else {
            startMyForegroundService();
        }
    }

    private void startMyForegroundService() {
        Intent intent = new Intent(this, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMyForegroundService();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền thông báo để thấy Foreground Service", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            tvResult.setText("AsyncTask 1 bắt đầu...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            for (int i = 1; i <= 3; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Hoàn thành AsyncTask 1!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tvResult.setText("AsyncTask 1 đang xử lý: " + values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            tvResult.setText(result);
        }
    }

    private void runModernAsyncTask() {
        tvResult.setText("Async 2 bắt đầu...");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            for (int i = 1; i <= 3; i++) {
                try {
                    Thread.sleep(1000);
                    final int progress = i;
                    handler.post(() -> tvResult.setText("Async 2 đang xử lý: " + progress));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.post(() -> tvResult.setText("Hoàn thành Async 2!"));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}