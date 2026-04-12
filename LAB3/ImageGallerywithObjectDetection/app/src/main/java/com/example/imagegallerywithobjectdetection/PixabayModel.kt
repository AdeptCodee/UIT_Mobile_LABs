package com.example.imagegallerywithobjectdetection

// 1. Khuôn lớn nhất chứa danh sách hình ảnh tải về
data class PixabayResponse(
    val hits: List<ImageItem>
)

// 2. Khuôn chứa thông tin chi tiết của 1 bức ảnh
data class ImageItem(
    val id: Int,
    val previewURL: String, // Link ảnh nhỏ
    val largeImageURL: String, // Link ảnh lớn
    val tags: String // Nhãn có sẵn của ảnh
)

// 3. Khuôn AI: Chứa ảnh VÀ chứa các từ khóa do AI tự nhận diện được
data class ProcessedImage(
    val image: ImageItem,
    val aiTags: List<String>
)