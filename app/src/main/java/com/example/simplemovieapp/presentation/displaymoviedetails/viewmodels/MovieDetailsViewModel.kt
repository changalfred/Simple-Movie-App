package com.example.simplemovieapp.presentation.displaymoviedetails.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.asDatabaseEntity
import com.example.simplemovieapp.data.local.MovieDatabase
import com.example.simplemovieapp.presentation.models.MovieDetailsPresentationEntity
import com.example.simplemovieapp.utilities.NOT_SAVED
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieDetailsViewModel(
    applicationContext: Application
) : ViewModel() {

    private val database = MovieDatabase.invoke(applicationContext)
    private val repository = MoviesRepository(database)

    private val _movieDetails = MutableLiveData<MovieDetailsPresentationEntity>()
    val movieDetails: LiveData<MovieDetailsPresentationEntity> = _movieDetails

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    fun getMovieDetails(id: Int, apiKey: String, language: String?) = viewModelScope.launch {
        try {
            val response = repository.getMovieDetails(id, apiKey, language)
            _movieDetails.postValue(response!!)
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

    fun checkIfMovieSaved(id: Int) = viewModelScope.launch {
        repository.checkIfMovieSaved(id)
            .catch { e -> Timber.d("Exception: ${e.message}") }
            .collect { isSaved ->
                if (isSaved == NOT_SAVED) _isSaved.postValue(false)
                else _isSaved.postValue(true)
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

}