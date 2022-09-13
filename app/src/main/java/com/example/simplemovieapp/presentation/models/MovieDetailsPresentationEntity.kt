package com.example.simplemovieapp.presentation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailsPresentationEntity(
    val title: String,
    val backdropPath: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: String,
    val revenue: Int,
    val rating: Double
) : Parcelable