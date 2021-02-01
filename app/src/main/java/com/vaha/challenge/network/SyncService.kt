package com.vaha.challenge.network

import MovieSearchResult
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.github.kittinunf.fuel.httpGet
import com.vaha.challenge.model.Error
import com.vaha.challenge.model.movie.Movies
import org.greenrobot.eventbus.EventBus

class SyncService : JobIntentService() {


    /**
     * TODO: use intent action to grow datatypes and network operations here
     * e.g.:
     * CREATE_MOVIE, GET_MOVIES, UPDATE_MOVIE, DELETE_MOVIE,
     * CREATE_BOOK, GET_BOOKS, UPDATE_BOOKS, DELETE_BOOKS etc.
     */
    override fun onHandleWork(intent: Intent) {
        url.httpGet(
            listOf(
                API_KEY to apiKey,
                QUERY to intent.getStringExtra(QUERY),
                PAGE to intent.getIntExtra(PAGE, 1)
            )
        ).responseObject(MovieSearchResult.Deserializer()) { request, response, result ->
            val (movieSearchResult, err) = result
            if (err != null) {
                EventBus.getDefault().post(Error())
            } else {
                if (!movieSearchResult!!.results.isEmpty()) {
                    Movies.cacheQuery(this, intent.getStringExtra(QUERY)!!)
                }
                EventBus.getDefault().post(movieSearchResult)
            }
        }
    }

    companion object {

        val url = "https://api.themoviedb.org/3/search/movie"
        val posterUrl = "https://image.tmdb.org/t/p/w"
        val API_KEY = "api_key"
        val QUERY = "query"
        val apiKey = "2696829a81b1b5827d515ff121700838"
        val PAGE = "page"
        val ID = "id"

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, SyncService::class.java, 1, intent)
        }
    }


}