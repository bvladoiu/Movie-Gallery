package com.vaha.challenge.model.movie

import Movie


/**
 * Vary basic in memory cache that can be accessed from the ui thread atomically
 */
object MovieCache {

    private val movies: HashMap<Int, Movie> = HashMap()

    fun cache(movie: Movie) {
        movies.put(movie.id, movie)
    }

    fun load(id: Int): Movie? {
        return movies.get(id)
    }

}