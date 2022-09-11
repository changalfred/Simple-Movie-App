package com.example.simplemovieapp.ui.movies.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.database.MovieDatabase
import com.example.simplemovieapp.data.network.models.Movies
import com.example.simplemovieapp.ui.models.UiMovie
import com.example.simplemovieapp.ui.models.asUiModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
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

    private val _movies = MutableLiveData<Movies>()
    val movies: LiveData<Movies> = _movies

    private val moviesEventChannel = Channel<MoviesEvent>()
    val moviesEvent = moviesEventChannel.receiveAsFlow()

    fun getPopularMovies(
        apiKey: String,
        language: String?,
        region: String?)
    = viewModelScope.launch {
        try {
            val response = moviesRepository.getPopularMovies(apiKey, language, popularMoviesPage, region)
            _movies.postValue(response.body())
            _popularMovies.postValue(concatPreviousResults(response))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

    fun onMovieClicked(id: Int, title: String) = viewModelScope.launch {
        moviesEventChannel.send(MoviesEvent.NavigateToMovieDetailsScreen(id, title))
    }

    private fun concatPreviousResults(response: Response<Movies>): List<UiMovie> {
        response.body()?.let { movies ->
            popularMoviesPage++

            popularMoviesResponse = if (popularMoviesResponse == null) {
                movies.asUiModel()
            } else {
                val oldMovies = popularMoviesResponse
                val newMovies = movies.asUiModel()      // Better to use asUiModel() in the repository than here.
                val newList = mutableListOf<UiMovie>()
                oldMovies?.let { newList.addAll(oldMovies) }
                newList.addAll(newMovies)
                
                newList     // For some reason, can't just return newList on line 69 but have to assign newList to popularMoviesResponse
                            // and return that. May have something to do with newList being local var. but popularMoviesResponse being
                            // global var.
            }

            return popularMoviesResponse as List<UiMovie>
        }

        return emptyList()
    }

    sealed class MoviesEvent {
        data class NavigateToMovieDetailsScreen(val id: Int, val title: String) : MoviesEvent()
    }

}