package com.example.simplemovieapp.data.remote

import com.example.simplemovieapp.data.remote.models.MovieDetails
import com.example.simplemovieapp.data.remote.models.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = "en-US",
        @Query("page") page: Int? = 1,
        @Query("region") region: String? = "US",
    ): Response<Movies>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = "en-US"
    ): Response<MovieDetails>

}