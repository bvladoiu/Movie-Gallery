package com.vaha.challenge.ui.moviedetail

import Movie
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.vaha.challenge.R
import com.vaha.challenge.model.movie.MovieCache
import com.vaha.challenge.network.SyncService
import com.vaha.challenge.ui.GraphicUtil.getRecommendedPosterWidth
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetail : AppCompatActivity() {

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        movie = MovieCache.load(intent.extras!!.getInt("id"))!!
        actionBar?.title = movie.title

        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = movie.title
        rating.text = "" + movie.vote_average
        totalVotes.text = "" + movie.vote_count
        overview.text = movie.overview + movie.overview + movie.overview + movie.overview
        Glide.with(this)
            .load(SyncService.posterUrl + getRecommendedPosterWidth(display!!.width) + movie.poster_path)
            .transform(FitCenter())
            .placeholder(R.drawable.ic_movie_white_48dp)
            .error(R.drawable.ic_broken_image_white_48dp)
            .fallback(R.drawable.ic_movie_white_48dp)
            .into(poster)
    }

    /**
     * Using the back button behavior to prevent the parent activity from being recreated (home button behaviour)
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}