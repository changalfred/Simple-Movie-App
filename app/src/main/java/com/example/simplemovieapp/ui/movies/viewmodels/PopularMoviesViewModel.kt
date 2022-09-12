package com.example.simplemovieapp.ui.movies.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.local.MovieDatabase
import com.example.simplemovieapp.ui.models.UiMovie
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PopularMoviesViewModel(
    applicationContext: Application
) : ViewModel() {

    var popularMoviesPage = 1

    private var popularMoviesResponse: List<UiMovie>? = null

    private val database = MovieDatabase.invoke(applicationContext)
    private val moviesRepository = MoviesRepository(database)

    private val _popularMovies = MutableLiveData<List<UiMovie>>()
    val popularMovies: LiveData<List<UiMovie>> = _popularMovies

    private val _totalPages = MutableLiveData<Int>()
    val totalPages: LiveData<Int> = _totalPages

    private val moviesEventChannel = Channel<MoviesEvent>()
    val moviesEvent = moviesEventChannel.receiveAsFlow()

    // TODO: getTotalPages(...) and getPopularMovies(...) same calls.
    fun getTotalPages(
        apiKey: String,
        language: String?,
        region: String?
    ) = viewModelScope.launch {
        try {
            val response = moviesRepository.getTotalPages(apiKey, language, popularMoviesPage, region)
            _totalPages.postValue(response.body()?.totalPages)
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

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
            _popularMovies.postValue(concatPreviousResults(response))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

    fun onMovieClicked(id: Int, title: String) = viewModelScope.launch {
        moviesEventChannel.send(MoviesEvent.NavigateToMovieDetailsScreen(id, title))
    }

    private fun concatPreviousResults(response: List<UiMovie>?): List<UiMovie> {
        response.let { movies ->
            popularMoviesPage++

            popularMoviesResponse = if (popularMoviesResponse == null) {
                movies
            } else {
                val oldMovies = popularMoviesResponse
                val newList = mutableListOf<UiMovie>()
                oldMovies?.let { newList.addAll(oldMovies) }
                newList.addAll(movies as List<UiMovie>)

                newList
            }

            return popularMoviesResponse as List<UiMovie>
        }
    }

    sealed class MoviesEvent {
        data class NavigateToMovieDetailsScreen(val id: Int, val title: String) : MoviesEvent()
    }

}