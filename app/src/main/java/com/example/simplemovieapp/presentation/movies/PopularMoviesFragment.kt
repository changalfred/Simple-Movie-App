package com.example.simplemovieapp.presentation.movies

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemovieapp.R
import com.example.simplemovieapp.databinding.FragmentMoviesBinding
import com.example.simplemovieapp.presentation.movies.viewmodels.PopularMoviesViewModel
import com.example.simplemovieapp.presentation.movies.viewmodels.PopularMoviesViewModelProviderFactory
import com.example.simplemovieapp.utilities.QUERY_SIZE_LIMIT

class PopularMoviesFragment : Fragment(R.layout.fragment_movies),
    MoviesAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var popularMoviesViewModel: PopularMoviesViewModel

    private var isScrolling = false
    private var isLastPage = false
    private var isLoading = false

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
                !isLoading && !isLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling

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

        setupViewModel()
        setupRecyclerView()
        setupObservables()
        getPopularMovies()
    }

    override fun onMovieClick(id: Int, title: String) {
        popularMoviesViewModel.onMovieClicked(id, title)
    }

    private fun getPopularMovies() =
        popularMoviesViewModel.getPopularMovies(getString(R.string.tmdb_api_key),
            "en-US", "US")

    private fun setupViewModel() {
        val viewModelProviderFactory =
            PopularMoviesViewModelProviderFactory(context?.applicationContext as Application)
        popularMoviesViewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[PopularMoviesViewModel::class.java]
    }

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
        popularMoviesViewModel.totalPages.observe(viewLifecycleOwner) { totalPages ->
            isLastPage = popularMoviesViewModel.popularMoviesPage == totalPages
        }

        // Channels
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            popularMoviesViewModel.moviesEvent.collect { moviesEvent ->
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
        binding.progressCircular.isVisible = false
    }

    private fun showProgressBar() {
        isLoading = true
        binding.progressCircular.isVisible = true
    }

}