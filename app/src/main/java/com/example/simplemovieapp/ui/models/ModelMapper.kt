package com.example.simplemovieapp.ui.models

import com.example.simplemovieapp.data.network.models.MovieDetails
import com.example.simplemovieapp.data.network.models.Movies

// Map network model to UI model.
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

// Map network model to UI model.
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


