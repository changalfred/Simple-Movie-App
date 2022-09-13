package com.example.simplemovieapp.data.remote.models

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<GenreX>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    @SerializedName("original_title") val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    val production_companies: List<ProductionCompanyX>,
    val production_countries: List<ProductionCountryX>,
    @SerializedName("release_date") val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguageX>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average") val rating: Double,
    val vote_count: Int
)