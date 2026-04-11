package com.example.exercise

// 1. Tầng ngoài cùng hứng toàn bộ dữ liệu trả về
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

// 2. Tầng chứa thông tin chi tiết của từng bài báo
data class Article(
//    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

//// 3. Tầng chứa thông tin nguồn cung cấp bài báo
//data class Source(
//    val id: String?,
//    val name: String
//)