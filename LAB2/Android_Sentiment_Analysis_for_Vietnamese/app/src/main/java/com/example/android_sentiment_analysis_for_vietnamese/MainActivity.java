package com.example.android_sentiment_analysis_for_vietnamese;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText edtInput;
    private Button btnSubmit;
    private ImageView imgSentiment;

    // Khởi tạo OkHttpClient để gọi API
    private OkHttpClient client = new OkHttpClient();

    // Địa chỉ server Flask (10.0.2.2 là localhost của máy tính khi dùng máy ảo Android Studio)
    private static final String API_URL = "http://10.0.2.2:5000/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtInput = findViewById(R.id.edt_input);
        btnSubmit = findViewById(R.id.btn_submit);
        imgSentiment = findViewById(R.id.img_sentiment);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtInput.getText().toString().trim();
                if (!text.isEmpty()) {
                    analyzeSentiment(text);
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập một câu!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void analyzeSentiment(String text) {
        // Tạo chuỗi JSON chứa câu văn cần phân tích
        String jsonString = "{\"text\": \"" + text + "\"}";
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));

        // Tạo Request gửi đi
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        // Đưa request vào hàng đợi và chạy ngầm (không làm đơ giao diện)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Báo lỗi lên giao diện chính
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Lấy kết quả JSON từ server Flask
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);

                        // Giả sử server trả về JSON format: {"sentiment": "POS"}
                        String sentimentLabel = jsonObject.getString("sentiment");

                        // Cập nhật giao diện theo kết quả
                        runOnUiThread(() -> updateSentimentIcon(sentimentLabel));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateSentimentIcon(String label) {
        // Thay đổi icon dựa vào kết quả PhoBERT phân tích (POS: Tích cực, NEG: Tiêu cực, NEU: Trung tính)
        switch (label) {
            case "POS":
                imgSentiment.setImageResource(R.drawable.icon_funny); // Đổi tên đúng file ảnh của bạn
                break;
            case "NEG":
                imgSentiment.setImageResource(R.drawable.icon_sadness);
                break;
            default:
                imgSentiment.setImageResource(R.drawable.icon_khcamxuc);
                break;
        }
    }
}