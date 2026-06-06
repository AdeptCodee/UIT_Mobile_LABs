package com.example.dictionaryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText editWord;
    Button btnLookup;
    TextView textResult;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ giao diện
        editWord = findViewById(R.id.editWord);
        btnLookup = findViewById(R.id.btnLookup);
        textResult = findViewById(R.id.textResult);

        // 1. Tạo cơ sở dữ liệu SQLite tên là "DictionaryDB"
        db = openOrCreateDatabase("DictionaryDB", MODE_PRIVATE, null);

        // 2. Tạo bảng 'words' với 2 cột: 'word' và 'definition'
        db.execSQL("CREATE TABLE IF NOT EXISTS words(word TEXT, definition TEXT)");

        // Xóa dữ liệu cũ để tránh bị trùng lặp khi chạy lại app nhiều lần
        db.execSQL("DELETE FROM words");

        // 3. Thêm dữ liệu mẫu bằng ContentValues
        ContentValues cvalues = new ContentValues();

        // Thêm từ mẫu thứ nhất
        cvalues.put("word", "verisimilitude");
        cvalues.put("definition", "Sự thật, tính xác thực");
        db.insert("words", null, cvalues);

        // Thêm từ mẫu thứ hai (để test tính năng tìm chuỗi ký tự)
        cvalues.put("word", "veri");
        cvalues.put("definition", "Một tiền tố hoặc từ ngắn");
        db.insert("words", null, cvalues);

        setupSearchButton();
    }
    private void setupSearchButton() {
        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tuKhoa = editWord.getText().toString();
                String ketQua = "";

                // Lần 1: Tìm chính xác từ vừa nhập
                Cursor cursor = db.rawQuery("SELECT * FROM words WHERE word = ?", new String[]{tuKhoa});

                // Nếu tìm thấy từ chính xác
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                    String definition = cursor.getString(cursor.getColumnIndexOrThrow("definition"));
                    ketQua = word + " : " + definition;
                }
                // Nếu không tìm thấy từ chính xác, chuyển sang tìm chuỗi con (substring)
                else {
                    // Ký tự % ở trước và sau dấu ? giúp tìm chuỗi con
                    cursor = db.rawQuery("SELECT * FROM words WHERE word LIKE ?", new String[]{"%" + tuKhoa + "%"});

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        // Dùng vòng lặp do...while để duyệt qua tất cả kết quả
                        do {
                            String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                            String definition = cursor.getString(cursor.getColumnIndexOrThrow("definition"));
                            ketQua = ketQua + word + " : " + definition + "\n";
                        } while (cursor.moveToNext());
                    } else {
                        ketQua = "Không tìm thấy kết quả nào.";
                    }
                }

                textResult.setText(ketQua);

                cursor.close(); // [cite: 246]
            }
        });
    }
}