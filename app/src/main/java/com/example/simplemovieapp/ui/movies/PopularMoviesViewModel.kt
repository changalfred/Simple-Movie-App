package com.example.simplemovieapp.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.ui.models.Movie
import com.example.simplemovieapp.ui.models.asUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

class PopularMoviesViewModel : ViewModel() {

    private val moviesRepository = MoviesRepository()

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    fun getPopularMovies(
        language: String?,
        page: Int?,
        region: String?)
    = viewModelScope.launch {
        try {
            val response = moviesRepository.getPopularMovies(language, page, region)

            Timber.d("Response body after as ui model: ${response.body()?.asUiModel()}")

            _popularMovies.postValue(response.body()?.asUiModel())
        } catch (e: Exception) {
            Timber.d("Exception: ${e.message}")
        }
    }

}