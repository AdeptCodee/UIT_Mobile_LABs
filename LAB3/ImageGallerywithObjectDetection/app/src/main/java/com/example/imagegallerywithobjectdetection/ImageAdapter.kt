package com.example.imagegallerywithobjectdetection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load // Thư viện tải ảnh

// Adapter này dùng dữ liệu là ProcessedImage (chứa cả ảnh và nhãn AI)
class ImageAdapter : PagingDataAdapter<ProcessedImage, ImageAdapter.ImageViewHolder>(IMAGE_COMPARATOR) {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgPicture: ImageView = view.findViewById(R.id.imgPicture)
        private val tvAiTags: TextView = view.findViewById(R.id.tvAiTags)

        fun bind(item: ProcessedImage) {
            // Dùng thư viện Coil để tải ảnh từ link trên mạng
            imgPicture.load(item.image.largeImageURL) {
                crossfade(true)
            }

            // Hiển thị các từ khóa do AI nhận diện được (ghép bằng dấu phẩy)
            if (item.aiTags.isNotEmpty()) {
                tvAiTags.text = "AI phát hiện: " + item.aiTags.joinToString(", ")
            } else {
                tvAiTags.text = "AI không nhận diện được"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<ProcessedImage>() {
            override fun areItemsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage): Boolean {
                // Mỗi bức ảnh trên Pixabay đều có một ID duy nhất
                return oldItem.image.id == newItem.image.id
            }

            override fun areContentsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage): Boolean {
                return oldItem == newItem
            }
        }
    }
}