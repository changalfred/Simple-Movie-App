package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.network.RetrofitInstance

class MoviesRepository {

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
}