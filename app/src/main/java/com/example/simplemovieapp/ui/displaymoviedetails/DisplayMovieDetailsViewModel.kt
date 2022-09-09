package com.example.simplemovieapp.ui.displaymoviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.ui.models.UiMovieDetailsModel
import com.example.simplemovieapp.ui.models.asUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

class DisplayMovieDetailsViewModel : ViewModel() {

    private val moviesRepository = MoviesRepository()

    private val _movieDetails = MutableLiveData<UiMovieDetailsModel>()
    val movieDetails: LiveData<UiMovieDetailsModel> = _movieDetails

    fun getMovieDetails(id: Int, apiKey: String, language: String?) = viewModelScope.launch {
        try {
            val response = moviesRepository.getMovieDetails(id, apiKey, language)
            _movieDetails.postValue(response.body()?.asUiModel())
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

}