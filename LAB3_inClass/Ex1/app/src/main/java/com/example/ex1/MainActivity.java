package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<User> arrayUser;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.myListView);
        arrayUser = new ArrayList<>();

        // Thêm 20 người dùng vào danh sách
        arrayUser.add(new User("Trương Mạnh Nguyên", "Nha Trang", R.mipmap.ic_launcher));
        arrayUser.add(new User("Harry", "San Diego", R.mipmap.ic_launcher));
        arrayUser.add(new User("Marla", "San Francisco", R.mipmap.ic_launcher));
        arrayUser.add(new User("Sarah", "San Marco", R.mipmap.ic_launcher));
        arrayUser.add(new User("Zhongli", "Liyue", R.mipmap.ic_launcher));
        arrayUser.add(new User("Venti", "Mondstadt", R.mipmap.ic_launcher));
        arrayUser.add(new User("Raiden", "Inazuma", R.mipmap.ic_launcher));
        arrayUser.add(new User("Nahida", "Sumeru", R.mipmap.ic_launcher));
        arrayUser.add(new User("Furina", "Fontaine", R.mipmap.ic_launcher));
        arrayUser.add(new User("Mavuika", "Natlan", R.mipmap.ic_launcher));
        arrayUser.add(new User("Sinh viên A", "VNU-HCM", R.mipmap.ic_launcher));
        arrayUser.add(new User("Sinh viên B", "UIT", R.mipmap.ic_launcher));
        arrayUser.add(new User("David", "London", R.mipmap.ic_launcher));
        arrayUser.add(new User("Emma", "Paris", R.mipmap.ic_launcher));
        arrayUser.add(new User("Lisa", "Tokyo", R.mipmap.ic_launcher));
        arrayUser.add(new User("Kevin", "Seoul", R.mipmap.ic_launcher));
        arrayUser.add(new User("Anna", "Berlin", R.mipmap.ic_launcher));
        arrayUser.add(new User("Peter", "Sydney", R.mipmap.ic_launcher));
        arrayUser.add(new User("Laura", "Toronto", R.mipmap.ic_launcher));
        arrayUser.add(new User("Tom", "New York", R.mipmap.ic_launcher));

        adapter = new UserAdapter(this, R.layout.item_user, arrayUser);
        listView.setAdapter(adapter);
    }
}