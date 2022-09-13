package com.example.simplemovieapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieCacheEntity(
    @PrimaryKey val id: Int,    // Same as movie id from network model.
    val title: String,
    val backdropPath: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: String,
    val revenue: Int,           // Get from network model MovieDetails.
    val rating: Double,         // Same vote_average in network model.
    val isSaved: Boolean = false
)