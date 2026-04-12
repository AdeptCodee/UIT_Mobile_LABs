package com.example.exercise

import androidx.paging.PagingSource
import androidx.paging.PagingState

class NewsPagingSource(
    private val service: NewsApiService,
    private val apiKey: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            // Nếu mới mở ứng dụng, bắt đầu tải từ trang 1
            val page = params.key ?: 1

            // Gọi cỗ máy Retrofit đi lấy tin tức về nội dung của query
            val response = service.getArticles(
                query = "makeup",
                pageSize = params.loadSize,
                page = page,
                apiKey = apiKey
            )

            // Trả kết quả về cho ứng dụng và tính toán trang tiếp theo
            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            // Báo lỗi nếu rớt mạng
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }
}