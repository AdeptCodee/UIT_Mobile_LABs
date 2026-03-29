package com.example.ex_flappy_x;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView btn_play;
    CardView btn_credits;
    View profileLayout;
    TextView txt_credits_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_credits = findViewById(R.id.btn_credits);
        profileLayout = findViewById(R.id.include_profile);
        txt_credits_label = findViewById(R.id.txt_credits_label);
        btn_play = findViewById(R.id.btn_play);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Play!!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileLayout.getVisibility() == View.GONE) {
                    profileLayout.setVisibility(View.VISIBLE);
                    txt_credits_label.setText("Back"); // Đổi chữ nút thành Back

                    // Ẩn nút Play và các thành phần khác , màn hình sạch hơn
                    btn_play.setVisibility(View.GONE);
                }
                // nếu Profile đang hiện thì ẩn nó đi
                else {
                    profileLayout.setVisibility(View.GONE);
                    txt_credits_label.setText("Ex1"); // Đổi chữ nút quay lại ban đầu

                    // Hiện lại nút Play
                    btn_play.setVisibility(View.VISIBLE);
                }
            }

        });
    }
}
