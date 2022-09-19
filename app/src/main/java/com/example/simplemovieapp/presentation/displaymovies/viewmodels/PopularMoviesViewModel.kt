package com.example.simplemovieapp.presentation.displaymovies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.presentation.models.MoviePresentationEntity
import com.example.simplemovieapp.utilities.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    var popularMoviesPage = 1
    var shouldFetchNextMovies = true

    private var popularMoviesResponse: List<MoviePresentationEntity>? = null

    private val _popularMovies = MutableLiveData<ResourceState<List<MoviePresentationEntity>>>()
    val popularMovies: LiveData<ResourceState<List<MoviePresentationEntity>>> = _popularMovies

    private val moviesStateChannel = Channel<MoviesState>()
    val moviesState = moviesStateChannel.receiveAsFlow()

    private val moviesEventChannel = Channel<MoviesEvent>()
    val moviesEvent = moviesEventChannel.receiveAsFlow()

    fun getPopularMovies() = viewModelScope.launch {
        if (shouldFetchNextMovies) {
            _popularMovies.postValue(ResourceState.Loading())

            try {
                val response = repository.getPopularMovies(page = popularMoviesPage)

                // TODO: Solve double postValue(...) by making another class in data layer with first
                // function that only gets totalPages and second function gets movies.
                if (response != null) {
                    moviesStateChannel.send(MoviesState.Page(response.totalPages))
                    val allMovies = concatPreviousResults(response.movies)
                    _popularMovies.postValue(ResourceState.Success(allMovies))
                }
            } catch (e: Exception) {
                Timber.d("Exception: ${e.message}, \n ${e.stackTraceToString()}")
                ResourceState.Error(null, "Could not fetch pages and movies.")
            } finally {
                shouldFetchNextMovies = false
            }
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

    sealed class MoviesState {
        data class Page(val page: Int) : MoviesState()
    }

}