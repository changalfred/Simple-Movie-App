package com.example.simplemovieapp.data

import com.example.simplemovieapp.data.local.models.MovieCacheEntity
import com.example.simplemovieapp.data.remote.models.MovieDetails
import com.example.simplemovieapp.data.remote.models.MoviesNetworkEntity
import com.example.simplemovieapp.presentation.models.MovieDetailsPresentationEntity
import com.example.simplemovieapp.presentation.models.MoviePresentationEntity
import com.example.simplemovieapp.presentation.models.MoviesPresentationEntity

fun MoviesNetworkEntity.asPresentationEntity(): MoviesPresentationEntity {
    return MoviesPresentationEntity(
        totalPages = totalPages,
        movies = movies.map { movie ->
            MoviePresentationEntity(
                posterPath = movie.poster_path,
                overview = movie.overview,
                id = movie.id,
                title = movie.original_title,
                rating = movie.vote_average
            )
        }
    )
}

// TODO: Find a way to rename MovieDetails -> MovieDetailsNetworkEntity (custom GSON builder?)
fun MovieDetails.asPresentationEntity(): MovieDetailsPresentationEntity {
    return MovieDetailsPresentationEntity(
        title = originalTitle,
        backdropPath = backdropPath,
        posterPath = posterPath,
        overview = overview,
        releaseDate = releaseDate,
        revenue = revenue,
        rating = rating
    )
}

fun MovieDetailsPresentationEntity.asDatabaseEntity(id: Int, isSaved: Boolean): MovieCacheEntity {
    return MovieCacheEntity(
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
