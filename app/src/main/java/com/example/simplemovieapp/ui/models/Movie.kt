package com.example.simplemovieapp.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val posterPath: String,
    val overview: String,
    val id: Int,
    val title: String,
    val rating: Double
): Parcelable