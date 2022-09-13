package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.local.MovieDatabase
import com.example.simplemovieapp.data.local.models.MovieCacheEntity
import com.example.simplemovieapp.data.remote.RetrofitInstance

class MoviesRepository(
    private val database: MovieDatabase
) {

    // Local calls.
    fun checkIfMovieSaved(id: Int) = database.getMovieDao().checkIfMovieSaved(id)

    suspend fun insertMovie(movie: MovieCacheEntity) = database.getMovieDao().insertMovie(movie)

    suspend fun deleteMovie(movie: MovieCacheEntity) = database.getMovieDao().deleteMovie(movie)


    // Remote calls.
    suspend fun getPopularMovies(
        apiKey: String,
        language: String?,
        page: Int?,
        region: String?
    ) = RetrofitInstance.moviesService.getPopularMovies(apiKey, language, page, region).body()
        ?.asPresentationEntity()

    suspend fun getMovieDetails(
        id: Int,
        apiKey: String,
        language: String?
    ) = RetrofitInstance.moviesService.getMovieDetails(id, apiKey, language).body()
        ?.asPresentationEntity()

}