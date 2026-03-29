package com.example.speech_to_text_translator;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 100;

    private TextView txtOriginal, txtTranslated;
    private Button btnRecord;
    private Spinner langSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ
        txtOriginal = findViewById(R.id.txt_original);
        txtTranslated = findViewById(R.id.txt_translated);
        btnRecord = findViewById(R.id.btn_record);
        langSpinner = findViewById(R.id.lang_spinner);

        // 2. Bắt sự kiện click cho nút ghi âm
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
    }

    // Hàm gọi hộp thoại nhận diện giọng nói
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN"); // Mặc định nhận diện tiếng Việt
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói gì đó...");

    }

    // Hàm nhận kết quả trả về sau khi user nói
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            // Lấy văn bản giọng nói
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = result.get(0);

            // Hiển thị văn bản gốc
            txtOriginal.setText(spokenText);
            txtTranslated.setText("...");

            // Gọi hàm dịch văn bản
            translateText(spokenText);
        }
    }

    // Hàm thực hiện dịch bằng ML Kit
    private void translateText(String textToTranslate) {
        // Lấy vị trí ngôn ngữ được chọn trong Spinner
        int selectedLangPosition = langSpinner.getSelectedItemPosition();
        String targetLangCode;

        // Map vị trí với mã ngôn ngữ của ML Kit (dựa theo thứ tự trong strings.xml)
        switch (selectedLangPosition) {
            case 1: targetLangCode = TranslateLanguage.ENGLISH; break;
            case 2: targetLangCode = TranslateLanguage.SPANISH; break;
            case 3: targetLangCode = TranslateLanguage.FRENCH; break;
            default: targetLangCode = TranslateLanguage.VIETNAMESE; break; // 0 là tiếng Việt
        }

        // Cấu hình Translator: Từ Tiếng Việt -> Ngôn ngữ đích
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.VIETNAMESE)
                .setTargetLanguage(targetLangCode)
                .build();

        final Translator translator = Translation.getClient(options);

        // Điều kiện tải mô hình ngôn ngữ (Chỉ tải khi có Wifi/Internet)
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Bước 2: Tải thành công thì bắt đầu dịch
                        translator.translate(textToTranslate)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        txtTranslated.setText(translatedText);
                                        translator.close(); // Đóng khi không dùng nữa để tiết kiệm RAM
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        txtTranslated.setText("Lỗi dịch: " + e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txtTranslated.setText("Lỗi");
                    }
                });
    }
}