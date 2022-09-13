package com.example.simplemovieapp.presentation.displaymoviedetails

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.simplemovieapp.R
import com.example.simplemovieapp.databinding.FragmentDisplayMovieDetailsBinding
import com.example.simplemovieapp.presentation.displaymoviedetails.viewmodels.MovieDetailsViewModel
import com.example.simplemovieapp.presentation.displaymoviedetails.viewmodels.MovieDetailsViewModelProviderFactory
import com.example.simplemovieapp.presentation.models.MovieDetailsPresentationEntity
import com.example.simplemovieapp.utilities.Formatters
import com.example.simplemovieapp.utilities.TMDB_IMAGE_BASE_URL
import com.example.simplemovieapp.utilities.W185
import com.example.simplemovieapp.utilities.W500

class MovieDetailsFragment : Fragment(R.layout.fragment_display_movie_details) {

    private lateinit var binding: FragmentDisplayMovieDetailsBinding
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var movie: MovieDetailsPresentationEntity

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDisplayMovieDetailsBinding.bind(view)

        setupViewModel()

        movieDetailsViewModel.getMovieDetails(args.id, getString(R.string.tmdb_api_key),
            "en-US")

        subscribeToObservables()
        updateSaveButton()
        setOnClickListeners()
    }

    private fun setupViewModel() {
        val viewModelProviderFactory =
            MovieDetailsViewModelProviderFactory(context?.applicationContext as Application)
        movieDetailsViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[MovieDetailsViewModel::class.java]
    }

    private fun subscribeToObservables() {
        movieDetailsViewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            movie = movieDetails
            bindViews(movieDetails)
        }
        movieDetailsViewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            setSaveButton(isSaved)
        }
    }

    private fun updateSaveButton() {
        movieDetailsViewModel.checkIfMovieSaved(args.id)
    }

    private fun setSaveButton(saved: Boolean) {
        if (!saved) {
            binding.imageviewSaved.visibility = View.INVISIBLE
            binding.imageviewNotSaved.visibility = View.VISIBLE
        } else {
            binding.imageviewSaved.visibility = View.VISIBLE
            binding.imageviewNotSaved.visibility = View.INVISIBLE
        }
    }

    private fun addToSaved() {
        movieDetailsViewModel.saveMovie(movie, args.id)
        updateSaveButton()
    }

    private fun removeFromSaved() {
        movieDetailsViewModel.unsaveMovie(movie, args.id)
        updateSaveButton()
    }

    private fun setOnClickListeners() {
        binding.apply {
            imageviewNotSaved.setOnClickListener {
                addToSaved()
            }
            imageviewSaved.setOnClickListener {
                removeFromSaved()
            }
            imageviewRate.setOnClickListener {

            }
            imageviewShare.setOnClickListener {

            }
        }
    }

    private fun bindViews(movieDetails: MovieDetailsPresentationEntity) {
        binding.apply {
            Glide.with(this@MovieDetailsFragment)
                .load(TMDB_IMAGE_BASE_URL + W500 + movieDetails.backdropPath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageviewMovieBackdrop)

            Glide.with(this@MovieDetailsFragment)
                .load(TMDB_IMAGE_BASE_URL + W185 + movieDetails.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageviewMoviePoster)

            textviewRating.text = Formatters.roundToNearestTenth(movieDetails.rating).toString()
            textviewReleaseDate.text = requireContext().getString(
                R.string.release_date,
                Formatters.formatDate(movieDetails.releaseDate)
            )
            textviewRevenue.text = requireContext().getString(
                R.string.revenue,
                Formatters.formatWithCommas(movieDetails.revenue)
            )
            textviewMovieOverview.text = movieDetails.overview
        }
    }

}