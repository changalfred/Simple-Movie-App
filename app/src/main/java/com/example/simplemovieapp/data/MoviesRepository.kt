package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.local.MovieDatabase
import com.example.simplemovieapp.data.local.models.SavedMovie
import com.example.simplemovieapp.data.remote.RetrofitInstance

class MoviesRepository(
   private val database: MovieDatabase
) {

    // Local calls.
    fun checkIfMovieSaved(id: Int) = database.getMovieDao().checkIfMovieSaved(id)

    suspend fun insertMovie(movie: SavedMovie) = database.getMovieDao().insertMovie(movie)

    suspend fun deleteMovie(movie: SavedMovie) = database.getMovieDao().deleteMovie(movie)


    // Remote calls.
    // TODO: Fix getTotalPages(...) and getPopularMovies(...) as both return same response.
    suspend fun getTotalPages(
        apiKey: String,
        language: String?,
        page: Int?,
        region: String?
    ) = RetrofitInstance.moviesService.getPopularMovies(apiKey, language, page, region)

    suspend fun getPopularMovies(
        apiKey: String,
        language: String?,
        page: Int?,
        region: String?
    ) = RetrofitInstance.moviesService.getPopularMovies(apiKey, language, page, region).body()?.asUiModel()

    suspend fun getMovieDetails(
        id: Int,
        apiKey: String,
        language: String?
    ) = RetrofitInstance.moviesService.getMovieDetails(id, apiKey, language).body()?.asUiModel()

}