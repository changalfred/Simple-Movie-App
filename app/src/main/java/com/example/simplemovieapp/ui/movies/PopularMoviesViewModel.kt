package com.example.simplemovieapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.network.models.PopularMovies
import kotlinx.coroutines.launch
import timber.log.Timber

class PopularMoviesViewModel : ViewModel() {

    private val moviesRepository = MoviesRepository()

    private val _popularMovies = MutableLiveData<PopularMovies>()
    val popularMovies: LiveData<PopularMovies> = _popularMovies

    fun getPopularMovies(
        language: String?,
        page: Int?,
        region: String?)
    = viewModelScope.launch {
        try {
            val response = moviesRepository.getPopularMovies(language, page, region)
            _popularMovies.postValue(response.body())
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

}