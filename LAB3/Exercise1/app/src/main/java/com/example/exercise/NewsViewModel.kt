package com.example.exercise // Đổi thành tên project của bạn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class NewsViewModel : ViewModel() {
    // BẠN PHẢI THAY DÒNG CHỮ NÀY BẰNG API KEY BẠN LẤY TRÊN TRANG WEB NEWSAPI.ORG
    private val apiKey = "e828aa9735c546408d84d76550c834f6"

    // Tạo luồng dữ liệu (Flow) tải mỗi lần 20 bài báo
    val articles = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { NewsPagingSource(RetrofitClient.instance, apiKey) }
    ).flow.cachedIn(viewModelScope) // Giữ dữ liệu sống sót trong ViewModel
}