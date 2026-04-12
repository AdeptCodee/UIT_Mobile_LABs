package com.example.imagegallerywithobjectdetection

import androidx.paging.PagingSource
import androidx.paging.PagingState

// Lớp này nhận vào công cụ API, công cụ AI, chìa khóa API và từ khóa tìm kiếm
class ImagePagingSource(
    private val apiService: PixabayApiService,
    private val analyzer: ImageAnalyzer,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, ProcessedImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProcessedImage> {
        return try {
            val page = params.key ?: 1

            // 1. Gọi Pixabay để lấy danh sách ảnh
            val response = apiService.searchImages(
                apiKey = apiKey,
                query = query,
                page = page,
                perPage = params.loadSize
            )

            // 2. Chạy vòng lặp: Đưa từng bức ảnh cho AI quét
            val processedImages = response.hits.map { imageItem ->
                // Lấy đường link ảnh nhỏ đưa cho AI quét
                val aiTags = analyzer.analyzeImage(imageItem.previewURL)

                // Gộp bức ảnh và danh sách từ khóa AI lại với nhau
                ProcessedImage(
                    image = imageItem,
                    aiTags = aiTags
                )
            }

            // 3. Trả kết quả đã xử lý xong cho appp
            LoadResult.Page(
                data = processedImages,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.hits.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProcessedImage>): Int? {
        return state.anchorPosition
    }
}