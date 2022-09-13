package com.example.simplemovieapp.presentation.displaymoviedetails.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieDetailsViewModelProviderFactory(
    private val applicationContext: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(applicationContext) as T
    }

}