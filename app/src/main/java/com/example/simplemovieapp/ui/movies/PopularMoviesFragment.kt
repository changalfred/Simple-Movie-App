package com.example.simplemovieapp.ui.movies

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemovieapp.R
import com.example.simplemovieapp.databinding.FragmentMoviesBinding
import com.example.simplemovieapp.utils.Constants.QUERY_SIZE_LIMIT

class PopularMoviesFragment : Fragment(R.layout.fragment_movies),
    MoviesAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter

    private var isScrolling = false
    private var isLastPage = false
    private var isLoading = false

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

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isAtLastItem =
                firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_SIZE_LIMIT
            val shouldPaginate =
                !isLoading && !isLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                showProgressBar()

                popularMoviesViewModel.getPopularMovies(
                    getString(R.string.tmdb_api_key),
                    "en-US",
                    "US"
                )
                isScrolling = false
            }
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

    override fun onMovieClick(id: Int, title: String) {
        popularMoviesViewModel.onMovieClicked(id, title)
    }

    private fun getPopularMovies() =
        popularMoviesViewModel.getPopularMovies(getString(R.string.tmdb_api_key), "en-US", "US")

    private fun setupRecyclerView() {
        binding.recyclerviewMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(this@PopularMoviesFragment.scrollListener)
        }
    }

    private fun setupObservables() {
        // LiveData
        popularMoviesViewModel.popularMovies.observe(viewLifecycleOwner) { popularMovies ->
            hideProgressBar()
            moviesAdapter.differ.submitList(popularMovies)
        }
        popularMoviesViewModel.movies.observe(viewLifecycleOwner) { moviesResponse ->
            val totalPages = moviesResponse.totalPages
            isLastPage = popularMoviesViewModel.popularMoviesPage == totalPages
        }

        // Channels
        // TODO: What does the next line do?
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            popularMoviesViewModel.moviesEvent.collect { moviesEvent ->
                // There could be more than one event in future, so using when statement here.
                when (moviesEvent) {
                    is PopularMoviesViewModel.MoviesEvent.NavigateToMovieDetailsScreen -> {
                        val action =
                            PopularMoviesFragmentDirections.actionMoviesFragmentToDisplayMovieDetailsFragment(
                                moviesEvent.id,
                                moviesEvent.title
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        isLoading = false
        binding.progressBar.isVisible = false
    }

    private fun showProgressBar() {
        isLoading = true
        binding.progressBar.isVisible = true
    }

}