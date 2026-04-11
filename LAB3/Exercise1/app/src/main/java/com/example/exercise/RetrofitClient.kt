package com.example.exercise // Đổi thành tên project của bạn

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Địa chỉ gốc của trang web cung cấp tin tức
    private const val BASE_URL = "https://newsapi.org/"

    // Cỗ máy Retrofit sẽ tự động dịch dữ liệu JSON thành các Data Class (Article)
    val instance: NewsApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(NewsApiService::class.java)
    }
}