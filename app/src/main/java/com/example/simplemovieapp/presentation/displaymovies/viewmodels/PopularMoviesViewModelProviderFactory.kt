package com.example.simplemovieapp.presentation.displaymovies.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PopularMoviesViewModelProviderFactory(
    private val applicationContext: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PopularMoviesViewModel(applicationContext) as T
    }

}