package com.example.mytvapplication

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class VideoDetailsFragment : DetailsSupportFragment() {

    private lateinit var mSelectedMovie: Movie
    private lateinit var mDetailsBackend: DetailsSupportFragmentBackgroundController
    private var mTarget: Target? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDetailsBackend = DetailsSupportFragmentBackgroundController(this)
        
        val movie = requireActivity().intent.getSerializableExtra(DetailsActivity.MOVIE)
        if (movie is Movie) {
            mSelectedMovie = movie
            setupDetailsOverviewRow()
            setupRelatedMovieRow()
        }
    }

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(mSelectedMovie)
        
        row.imageDrawable = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_gallery)
        
        mTarget = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                row.setImageBitmap(requireContext(), bitmap)
                mDetailsBackend.coverBitmap = bitmap
            }
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                row.imageDrawable = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_report_image)
            }
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        }

        if (!mSelectedMovie.cardImageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(mSelectedMovie.cardImageUrl)
                .into(mTarget!!)
        }

        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())

        val presenterSelector = ClassPresenterSelector()
        presenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())

        val detailsAdapter = ArrayObjectAdapter(presenterSelector)
        detailsAdapter.add(row)
        adapter = detailsAdapter
    }

    private fun setupRelatedMovieRow() {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        listRowAdapter.add(Movie(101, "Season 1", mSelectedMovie.studio, "Nội dung phần 1", mSelectedMovie.cardImageUrl))
        listRowAdapter.add(Movie(102, "Season 2", mSelectedMovie.studio, "Nội dung phần 2", mSelectedMovie.cardImageUrl))
        
        val header = HeaderItem(0, "Seasons")
        val mainAdapter = adapter
        if (mainAdapter is ArrayObjectAdapter) {
            mainAdapter.add(ListRow(header, listRowAdapter))
        }
    }
}
