package com.example.exercise // Nhớ đổi tên chữ exercise thành tên project của bạn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import coil.load

// Kế thừa PagingDataAdapter, truyền vào kiểu dữ liệu (Article) và ViewHolder
class NewsAdapter : PagingDataAdapter<Article, NewsAdapter.ArticleViewHolder>(ARTICLE_COMPARATOR) {

    // 1. Lớp ViewHolder: Đây là "cái hộp" để lưu giữ các thành phần giao diện của 1 dòng
    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArticle: ImageView = view.findViewById(R.id.imgArticle)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDate: TextView = view.findViewById(R.id.tvDate)

        // Hàm này nhận dữ liệu từ 1 bài báo và "bơm" vào giao diện
        fun bind(article: Article) {
            tvTitle.text = article.title
            tvDate.text = article.publishedAt ?: "Không rõ ngày đăng" // Nếu không có ngày thì hiện chữ mặc định
            if (article.urlToImage != null) {
                // Nếu bài báo có ảnh, load ảnh đó lên
                imgArticle.load(article.urlToImage) {
                    crossfade(true) // Hiệu ứng mờ dần cho đẹp mắt
                }
                imgArticle.visibility = View.VISIBLE // Hiện khung ảnh
            } else {
                // Nếu bài báo không có ảnh, giấu cái khung ảnh đi
                imgArticle.visibility = View.GONE
            }
        }
    }

    // 2. Tạo ViewHolder mới (Chỉ chạy khi màn hình thiếu khung để hiển thị)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        // Nạp file giao diện item_article.xml vào
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    // 3. Gắn dữ liệu vào ViewHolder (Chạy liên tục khi bạn cuộn màn hình)
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article) // Gọi hàm bind() ở trên để đổ chữ vào
        }
    }

    // 4. Công cụ DiffUtil: Giúp Paging 3 biết bài báo nào mới, bài nào cũ để không vẽ trùng lặp
    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                // Kiểm tra xem 2 bài báo có cùng tiêu đề không (coi như ID)
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                // Kiểm tra xem toàn bộ nội dung có y hệt nhau không
                return oldItem == newItem
            }
        }
    }
}