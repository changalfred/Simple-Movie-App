package com.example.simplemovieapp.data.remote.models

import com.google.gson.annotations.SerializedName

data class MoviesNetworkEntity(
    val page: Int,
    @SerializedName("results") val movies: List<Result>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)