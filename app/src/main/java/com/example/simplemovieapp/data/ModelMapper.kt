package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.local.models.SavedMovie
import com.example.simplemovieapp.data.remote.models.MovieDetails
import com.example.simplemovieapp.data.remote.models.Movies
import com.example.simplemovieapp.ui.models.UiMovie
import com.example.simplemovieapp.ui.models.UiMovieDetails

// Remote -> UI
fun Movies.asUiModel(): List<UiMovie> {
    return results.map { movie ->
        UiMovie(
            posterPath = movie.poster_path,
            overview = movie.overview,
            id = movie.id,
            title = movie.original_title,
            rating = movie.vote_average
        )
    }
}

// Remote -> UI
fun MovieDetails.asUiModel(): UiMovieDetails {
    return UiMovieDetails(
        title = originalTitle,
        backdropPath = backdropPath,
        posterPath = posterPath,
        overview = overview,
        releaseDate = releaseDate,
        revenue = revenue,
        rating = rating
    )
}

// UI -> local
fun UiMovieDetails.asDbModel(id: Int, isSaved: Boolean): SavedMovie {
    return SavedMovie(
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
