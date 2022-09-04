package com.example.simplemovieapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.network.models.Movies
import com.example.simplemovieapp.ui.models.UiMovieModel
import com.example.simplemovieapp.ui.models.asUiModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class PopularMoviesViewModel : ViewModel() {

    var popularMoviesPage = 1

    private var popularMoviesResponse: List<UiMovieModel>? = null

    private val moviesRepository = MoviesRepository()

    private val _popularMovies = MutableLiveData<List<UiMovieModel>>()
    val popularMovies: LiveData<List<UiMovieModel>> = _popularMovies

    private val _movies = MutableLiveData<Movies>()
    val movies: LiveData<Movies> = _movies

    private val moviesEventChannel = Channel<MoviesEvent>() // TODO: Why use a channel here?
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

    // TODO: How was the pagination bug solved? (long process)
    private fun concatPreviousResults(response: Response<Movies>): List<UiMovieModel> {
        response.body()?.let { movies ->
            popularMoviesPage++

            if (popularMoviesResponse == null) {
                popularMoviesResponse = movies.asUiModel()
            } else {
                val oldMovies = popularMoviesResponse
                val newMovies = movies.asUiModel()
                val newList = mutableListOf<UiMovieModel>()
                oldMovies?.let { newList.addAll(oldMovies) }
                newList.addAll(newMovies)

                popularMoviesResponse = newList
            }

            return popularMoviesResponse as List<UiMovieModel>
        }

        return emptyList()
    }

    sealed class MoviesEvent {
        data class NavigateToMovieDetailsScreen(val id: Int, val title: String) : MoviesEvent()
    }

}