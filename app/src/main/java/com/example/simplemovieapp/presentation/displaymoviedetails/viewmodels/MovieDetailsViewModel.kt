package com.example.simplemovieapp.presentation.displaymoviedetails.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.asDatabaseEntity
import com.example.simplemovieapp.presentation.models.MovieDetailsPresentationEntity
import com.example.simplemovieapp.utilities.NOT_SAVED
import com.example.simplemovieapp.utilities.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _movieDetails = MutableLiveData<ResourceState<MovieDetailsPresentationEntity>>()
    val movieDetails: LiveData<ResourceState<MovieDetailsPresentationEntity>> = _movieDetails

    private val movieDetailsStateChannel = Channel<MovieDetailsState>(1)
    val movieDetailsState = movieDetailsStateChannel.receiveAsFlow()

    fun getMovieDetails(id: Int) = viewModelScope.launch {
        try {
            val response = repository.getMovieDetails(id)
            if (response != null) _movieDetails.postValue(ResourceState.Success(response))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
            _movieDetails.postValue(ResourceState.Error(null, "Could not fetch movie details."))
        }
    }

    fun checkIfMovieSaved(id: Int) = viewModelScope.launch {
        repository.checkIfMovieSaved(id)
            .collect { isSaved ->
                if (isSaved == NOT_SAVED) movieDetailsStateChannel.send(MovieDetailsState.MovieIsNotSaved)
                else movieDetailsStateChannel.send(MovieDetailsState.MovieIsSaved)
            }
    }

    fun saveMovie(movie: MovieDetailsPresentationEntity, id: Int) = viewModelScope.launch {
        try {
            repository.insertMovie(movie.asDatabaseEntity(id, true))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

    fun unsaveMovie(movie: MovieDetailsPresentationEntity, id: Int) = viewModelScope.launch {
        try {
            repository.deleteMovie(movie.asDatabaseEntity(id, false))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

    sealed class MovieDetailsState {
        object MovieIsSaved : MovieDetailsState()
        object MovieIsNotSaved : MovieDetailsState()
    }

}