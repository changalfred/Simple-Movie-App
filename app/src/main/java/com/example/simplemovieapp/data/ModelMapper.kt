package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.database.models.DbMovie
import com.example.simplemovieapp.ui.models.UiMovieDetails

// Map UI model to database model.
fun UiMovieDetails.asDbMovie(id: Int, isSaved: Boolean): DbMovie {
    return DbMovie(
        id = id,
        title = title,
        backdropPath = backdropPath,
        posterPath = posterPath,
        overview = overview,
        releaseDate = releaseDate,
        revenue = revenue,
        rating = rating,
        isSaved = isSaved
    )
}