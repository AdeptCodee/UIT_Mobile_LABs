package com.example.mytvapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : BrowseSupportFragment() {

    private lateinit var mRowsAdapter: ArrayObjectAdapter
    private val PAGE_SIZE = 10
    private val mCurrentPageMap = mutableMapOf<String, Int>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        setupAdapter()
        loadRows()
        setupEventListeners()
    }

    private fun setupUIElements() {
        title = "My TV Movies"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = Color.parseColor("#006666")
    }

    private fun setupAdapter() {
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = mRowsAdapter
    }

    private fun loadRows() {
        val categories = listOf("Series", "New Movies", "Old Movies")
        val cardPresenter = CardPresenter()

        viewLifecycleOwner.lifecycleScope.launch {
            categories.forEachIndexed { index, category ->
                mCurrentPageMap[category] = 0
                val movies = loadMoviesFromDb(category, 0)

                if (movies.isNotEmpty()) {
                    val listRowAdapter = ArrayObjectAdapter(cardPresenter)
                    listRowAdapter.addAll(0, movies)
                    val header = HeaderItem(index.toLong(), category)
                    mRowsAdapter.add(ListRow(header, listRowAdapter))
                }
            }
        }
    }

    private suspend fun loadMoviesFromDb(category: String, page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext(), viewLifecycleOwner.lifecycleScope)
            db.movieDao().getMoviesByCategoryPaged(category, PAGE_SIZE, page * PAGE_SIZE)
        }
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            if (item is Movie) {
                val intent = Intent(requireActivity(), DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item)
                startActivity(intent)
            }
        }

        onItemViewSelectedListener = OnItemViewSelectedListener { _, item, _, row ->
            if (row is ListRow && item is Movie) {
                val listRowAdapter = row.adapter as ArrayObjectAdapter
                val category = row.headerItem.name
                val selectedIndex = listRowAdapter.indexOf(item)

                if (selectedIndex >= listRowAdapter.size() - 2) {
                    val nextPage = (mCurrentPageMap[category] ?: 0) + 1
                    viewLifecycleOwner.lifecycleScope.launch {
                        val nextMovies = loadMoviesFromDb(category, nextPage)
                        if (nextMovies.isNotEmpty()) {
                            mCurrentPageMap[category] = nextPage
                            listRowAdapter.addAll(listRowAdapter.size(), nextMovies)
                        }
                    }
                }
            }
        }
    }
}
