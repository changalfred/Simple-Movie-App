package com.example.simplemovieapp.presentation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviePresentationEntity(
    val posterPath: String,
    val overview: String,
    val id: Int,
    val title: String,
    val rating: Double
) : Parcelable