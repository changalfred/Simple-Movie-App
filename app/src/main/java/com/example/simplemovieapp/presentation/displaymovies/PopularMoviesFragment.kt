package com.example.simplemovieapp.presentation.displaymovies

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
import com.example.simplemovieapp.presentation.displaymovies.viewmodels.PopularMoviesViewModel
import com.example.simplemovieapp.presentation.models.MoviePresentationEntity
import com.example.simplemovieapp.utilities.QUERY_SIZE_LIMIT
import com.example.simplemovieapp.utilities.ResourceState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularMoviesFragment : Fragment(R.layout.fragment_movies),
    MoviesAdapter.OnMovieClickListener {

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private lateinit var binding: FragmentMoviesBinding

    private var isScrolling = false
    private var isLastPage = false
    private var isLoading = false

    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels()

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

                popularMoviesViewModel.getPopularMovies()
                isScrolling = false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMoviesBinding.bind(view)

        setupRecyclerView()
        setupObservables()
        getPopularMovies()
    }

    override fun onMovieClick(id: Int, title: String) {
        popularMoviesViewModel.onMovieClicked(id, title)
    }

    private fun getPopularMovies() =
        popularMoviesViewModel.getPopularMovies()

    private fun setupRecyclerView() {
        binding.recyclerviewMovies.apply {
            adapter = moviesAdapter
            moviesAdapter.movieItemClickListener = this@PopularMoviesFragment
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(this@PopularMoviesFragment.scrollListener)
        }
    }

    private fun updateScreen(result: ResourceState<List<MoviePresentationEntity>>) {
        when (result) {
            is ResourceState.Loading -> {
                showProgressBar()
            }
            is ResourceState.Success -> {
                hideProgressBar()
                moviesAdapter.differ.submitList(result.data)
            }
            is ResourceState.Error -> {
                hideProgressBar()

                result.message?.let { message ->
                    Snackbar.make(requireView(), "Error: $message", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupObservables() {
        // LiveData
        popularMoviesViewModel.popularMovies.observe(viewLifecycleOwner) { popularMovies ->
            updateScreen(popularMovies)
        }
        popularMoviesViewModel.totalPages.observe(viewLifecycleOwner) { totalPages ->
            isLastPage = popularMoviesViewModel.popularMoviesPage == totalPages.data
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