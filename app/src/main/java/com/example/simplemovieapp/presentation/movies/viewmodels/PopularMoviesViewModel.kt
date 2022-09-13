package com.example.simplemovieapp.presentation.movies.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.local.MovieDatabase
import com.example.simplemovieapp.presentation.models.MoviePresentationEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PopularMoviesViewModel(
    applicationContext: Application
) : ViewModel() {

    var popularMoviesPage = 1

    private var popularMoviesResponse: List<MoviePresentationEntity>? = null

    private val database = MovieDatabase.invoke(applicationContext)
    private val moviesRepository = MoviesRepository(database)

    private val _popularMovies = MutableLiveData<List<MoviePresentationEntity>>()
    val popularMovies: LiveData<List<MoviePresentationEntity>> = _popularMovies

    private val _totalPages = MutableLiveData<Int>()
    val totalPages: LiveData<Int> = _totalPages

    private val moviesEventChannel = Channel<MoviesEvent>()
    val moviesEvent = moviesEventChannel.receiveAsFlow()

    fun getPopularMovies(
        apiKey: String,
        language: String?,
        region: String?
    ) = viewModelScope.launch {
        try {
            val response = moviesRepository.getPopularMovies(
                apiKey,
                language,
                popularMoviesPage,
                region
            )
            // TODO: Solve double postValue(...) by making another class in data layer with first
            // function that only gets totalPages and second function gets movies.
            _totalPages.postValue(response?.totalPages)
            _popularMovies.postValue(concatPreviousResults(response?.movies))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}, \n ${e.stackTraceToString()}")
        }
    }

    fun onMovieClicked(id: Int, title: String) = viewModelScope.launch {
        moviesEventChannel.send(MoviesEvent.NavigateToMovieDetailsScreen(id, title))
    }

    private fun concatPreviousResults(response: List<MoviePresentationEntity>?): List<MoviePresentationEntity> {
        response.let { movies ->
            popularMoviesPage++

            popularMoviesResponse = if (popularMoviesResponse == null) {
                movies
            } else {
                val oldMovies = popularMoviesResponse
                val newList = mutableListOf<MoviePresentationEntity>()
                oldMovies?.let { newList.addAll(oldMovies) }
                newList.addAll(movies as List<MoviePresentationEntity>)

                newList
            }

            return popularMoviesResponse as List<MoviePresentationEntity>
        }
    }

    sealed class MoviesEvent {
        data class NavigateToMovieDetailsScreen(val id: Int, val title: String) : MoviesEvent()
    }

}