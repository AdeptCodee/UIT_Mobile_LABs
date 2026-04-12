package com.example.exercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Khai báo các biến sẽ sử dụng
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Tìm cái RecyclerView mà đã thiết kế trong file activity_main.xml
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // 2. Quy định cách sắp xếp: Danh sách cuộn dọc từ trên xuống dưới
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 3. Khởi tạo Adapter
        adapter = NewsAdapter()
        recyclerView.adapter = adapter

        // 4. Gọi ViewModel
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        // 5. Mở một luồng chạy ngầm để liên tục lắng nghe dữ liệu từ mạng tải về
        lifecycleScope.launch {
            // collectLatest: Bất cứ khi nào tải xong 1 trang báo mới, nó sẽ báo về đây
            viewModel.articles.collectLatest { pagingData ->
                // Nạp dữ liệu vừa tải được vào Adapter để vẽ lên màn hình
                adapter.submitData(pagingData)
            }
        }
    }
}