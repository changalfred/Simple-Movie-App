package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.database.MovieDatabase
import com.example.simplemovieapp.data.database.models.DbMovie
import com.example.simplemovieapp.data.network.RetrofitInstance

class MoviesRepository(
    private val database: MovieDatabase
) {

    // Network calls.
    suspend fun getPopularMovies(
        apiKey: String,
        language: String?,
        page: Int?,
        region: String?
    ) = RetrofitInstance.moviesService.getPopularMovies(apiKey, language, page, region)

    suspend fun getMovieDetails(
        id: Int,
        apiKey: String,
        language: String?
    ) = RetrofitInstance.moviesService.getMovieDetails(id, apiKey, language)


    // Database calls.
    fun getSavedMovies() = database.getMovieDao().getSavedMovies()

    suspend fun checkIfMovieSaved(id: Int) = database.getMovieDao().getSavedMovieIfExists(id)

    suspend fun insertMovie(movie: DbMovie) = database.getMovieDao().insertMovie(movie)

    suspend fun deleteMovie(movie: DbMovie) = database.getMovieDao().deleteMovie(movie)
}