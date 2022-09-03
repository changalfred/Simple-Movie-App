package com.example.simplemovieapp.ui.movies

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemovieapp.R
import com.example.simplemovieapp.databinding.FragmentMoviesBinding
import com.example.simplemovieapp.ui.models.Movie
import timber.log.Timber

class PopularMoviesFragment : Fragment(R.layout.fragment_movies), MoviesAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter

    private var isScrolling = false
    private var isLastPage = false

    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels()

    // Pagination
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMoviesBinding.bind(view)
        moviesAdapter = MoviesAdapter(this)

        setupRecyclerView()
        setupObservables()
        getPopularMovies()
    }

    private fun getPopularMovies() =
        popularMoviesViewModel.getPopularMovies("en-US", 1, "US")

    private fun setupRecyclerView() {
        binding.recyclerviewMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(this@PopularMoviesFragment.scrollListener)
        }
    }

    private fun setupObservables() {
        popularMoviesViewModel.popularMovies.observe(viewLifecycleOwner) { popularMovies ->
            Timber.d("Popular movies: $popularMovies")

            moviesAdapter.differ.submitList(popularMovies)
        }
    }

    override fun onMovieClick(movie: Movie) {
    }

}