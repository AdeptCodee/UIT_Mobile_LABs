package com.example.imagegallerywithobjectdetection

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("api/")
    suspend fun searchImages(
        @Query("key") apiKey: String, // key API lấy trên web
        @Query("q") query: String,    // Từ khóa tìm kiếm
        @Query("page") page: Int,     // Số trang hiện tại
        @Query("per_page") perPage: Int, // Số ảnh muốn lấy trên 1 trang
        @Query("image_type") imageType: String = "photo" // Chỉ lấy hình ảnh (k lấy video)
    ): PixabayResponse // Trả về khuôn đã tạo
}