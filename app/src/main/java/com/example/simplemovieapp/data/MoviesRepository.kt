package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.network.RetrofitInstance
import com.example.simplemovieapp.utils.Constants

class MoviesRepository {

    // Network calls.
    suspend fun getPopularMovies(
        language: String?,
        page: Int?,
        region: String?
    ) = RetrofitInstance.vehicleService.getPopularMovies(Constants.TMDB_API_KEY, language, page, region)

}