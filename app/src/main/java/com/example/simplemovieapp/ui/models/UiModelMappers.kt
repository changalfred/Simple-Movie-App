package com.example.simplemovieapp.ui.models

import com.example.simplemovieapp.data.network.models.MovieDetails
import com.example.simplemovieapp.data.network.models.Movies

fun Movies.asUiModel(): List<UiMovieModel> {
    return results.map { movie ->
        UiMovieModel(
            posterPath = movie.poster_path,
            overview = movie.overview,
            id = movie.id,
            title = movie.original_title,
            rating = movie.vote_average
        )
    }
}

fun MovieDetails.asUiModel(): UiMovieDetailsModel {
    return UiMovieDetailsModel(
        title = this.originalTitle,
        backdropPath = this.backdropPath,
        posterPath = this.posterPath,
        overview = this.overview,
        releaseDate = this.releaseDate,
        revenue = this.revenue,
        rating = this.rating
    )
}


