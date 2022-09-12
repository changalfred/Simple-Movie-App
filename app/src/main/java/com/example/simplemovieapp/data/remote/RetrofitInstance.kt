package com.example.simplemovieapp.data.remote

import com.example.simplemovieapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val loggingClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.TMDB_BASE_URL)
        .client(loggingClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val moviesService: MoviesService = retrofit.create(MoviesService::class.java)

}