package com.example.simplemovieapp.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simplemovieapp.R
import timber.log.Timber

class PopularMoviesFragment : Fragment(R.layout.fragment_movies) {

    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservables()
        getPopularMovies()
    }

    private fun getPopularMovies() =
        popularMoviesViewModel.getPopularMovies("en-US", 1, "US")

    private fun setupObservables() {
        popularMoviesViewModel.popularMovies.observe(viewLifecycleOwner) { popularMovies ->
            Timber.d("Popular movies: $popularMovies")
        }
    }

}