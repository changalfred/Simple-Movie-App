package com.example.simplemovieapp.ui.displaymoviedetails.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.asDbMovie
import com.example.simplemovieapp.data.database.MovieDatabase
import com.example.simplemovieapp.ui.models.UiMovieDetails
import com.example.simplemovieapp.ui.models.asUiModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieDetailsViewModel(
    applicationContext: Application
) : ViewModel() {

    private val database = MovieDatabase.invoke(applicationContext)
    private val repository = MoviesRepository(database)

    private val _movieDetails = MutableLiveData<UiMovieDetails>()
    val movieDetails: LiveData<UiMovieDetails> = _movieDetails

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    fun getMovieDetails(id: Int, apiKey: String, language: String?) = viewModelScope.launch {
        try {
            val response = repository.getMovieDetails(id, apiKey, language)
            _movieDetails.postValue(response.body()?.asUiModel())
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

    fun checkIfFavourited(id: Int) = viewModelScope.launch {
        repository.checkIfMovieSaved(id)
            .catch { e -> Timber.d("Exception: ${e.message}") }
            .collect { isSaved ->
                Timber.d("MovieDetailsFragment: $isSaved")
                if (isSaved == 0) _isSaved.postValue(false)
                else _isSaved.postValue(true)
            }
    }

    fun getSavedMovies() {

    }

    fun saveMovie(movie: UiMovieDetails, id: Int) = viewModelScope.launch {
        try {
            repository.insertMovie(movie.asDbMovie(id, true))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

    fun unsaveMovie(movie: UiMovieDetails, id: Int) = viewModelScope.launch {
        try {
            repository.deleteMovie(movie.asDbMovie(id, false))
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

}