package com.example.imagegallerywithobjectdetection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class ImageViewModel(application: Application) : AndroidViewModel(application) {

    private val apiKey = "55398804-b4e0b4e05b68af916fe998480"

    // Khởi tạo AI nhận diện hình ảnh
    private val analyzer = ImageAnalyzer(application.applicationContext)

    // Tạo luồng dữ liệu phân trang. Tạm thời tìm từ khóa "animals"
    val images = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            ImagePagingSource(RetrofitClient.instance, analyzer, apiKey, "animals")
        }
    ).flow.cachedIn(viewModelScope)
}