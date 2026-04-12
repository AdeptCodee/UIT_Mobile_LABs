package com.example.imagegallerywithobjectdetection

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class ImageAnalyzer(private val context: Context) {
    // Khởi tạo công cụ AI của Google
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    // Hàm nhận vào một đường link ảnh, trả về danh sách các chữ mô tả ảnh đó
    suspend fun analyzeImage(imageUrl: String): List<String> = coroutineScope {
        try {
            // Dùng Coil tải ảnh từ link trên mạng về và biến thành dạng Bitmap (dữ liệu điểm ảnh)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = coil.Coil.imageLoader(context).execute(request)
            val bitmap = result.drawable?.toBitmap()

            // Nếu tải ảnh thành công, đưa ảnh cho AI xử lí
            if (bitmap != null) {
                val image = InputImage.fromBitmap(bitmap, 0)
                val labels = labeler.process(image).await()

                // AI trả về nhiều kết quả, chỉ lấy 3 kết quả chính xác nhất
                return@coroutineScope labels.take(3).map { it.text }
            } else {
                return@coroutineScope emptyList()
            }

        } catch (e: Exception) {
            // Nếu có lỗi (ví dụ rớt mạng), trả về danh sách rỗng
            return@coroutineScope emptyList()
        }
    }
}