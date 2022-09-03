package com.example.simplemovieapp.data.network

import com.example.simplemovieapp.data.network.models.Movies
import com.example.simplemovieapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.TMDB_API_KEY,
        @Query("language") language: String? = "en-US",
        @Query("page") page: Int? = 1,
        @Query("region") region: String? = "US",
    ): Response<Movies>



}