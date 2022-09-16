package com.example.simplemovieapp.data

import android.content.Context
import com.example.simplemovieapp.R
import com.example.simplemovieapp.data.local.MovieDatabase
import com.example.simplemovieapp.data.local.models.MovieCacheEntity
import com.example.simplemovieapp.data.remote.MoviesService

class MoviesRepository(
    private val context: Context,
    private val database: MovieDatabase,
    private val moviesService: MoviesService
) {

    // Local calls.
    fun checkIfMovieSaved(id: Int) = database.getMovieDao().checkIfMovieSaved(id)

    suspend fun insertMovie(movie: MovieCacheEntity) = database.getMovieDao().insertMovie(movie)

    suspend fun deleteMovie(movie: MovieCacheEntity) = database.getMovieDao().deleteMovie(movie)


    // Remote calls.
    suspend fun getPopularMovies(
        apiKey: String = context.getString(R.string.tmdb_api_key),
        language: String? = "en-US",
        page: Int? = 1,
        region: String? = "US"
    ) = moviesService.getPopularMovies(apiKey, language, page, region).body()
        ?.asPresentationEntity()

    suspend fun getMovieDetails(
        id: Int,
        apiKey: String = context.getString(R.string.tmdb_api_key),
        language: String? = "en-US"
    ) = moviesService.getMovieDetails(id, apiKey, language).body()
        ?.asPresentationEntity()

}