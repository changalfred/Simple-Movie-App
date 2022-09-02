package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.network.RetrofitInstance

class MoviesRepository {

    // Network calls.
    suspend fun getPopularMovies(
        language: String?,
        page: Int?,
        region: String
    ) = RetrofitInstance.vehicleService.getPopularMovies("", language, page, region)

}