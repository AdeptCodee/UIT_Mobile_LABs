package com.example.imagegallerywithobjectdetection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ImageViewModel
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo danh sách
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ImageAdapter()
        recyclerView.adapter = adapter

        // Khởi tạo ViewModel
        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        // Lắng nghe dữ liệu tải về và đưa vào Adapter
        lifecycleScope.launch {
            viewModel.images.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}