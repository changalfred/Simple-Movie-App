package com.example.simplemovieapp.ui.models

import com.example.simplemovieapp.data.network.models.Movies

fun Movies.asUiModel(): List<Movie> {
    return results.map { movie ->
        Movie(
            posterPath = movie.poster_path,
            overview = movie.overview,
            id = movie.id,
            title = movie.original_title,
            rating = movie.vote_average
        )
    }
}