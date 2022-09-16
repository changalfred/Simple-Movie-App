package com.example.simplemovieapp.presentation.displaymoviedetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.example.simplemovieapp.R
import com.example.simplemovieapp.databinding.FragmentDisplayMovieDetailsBinding
import com.example.simplemovieapp.presentation.displaymoviedetails.viewmodels.MovieDetailsViewModel
import com.example.simplemovieapp.presentation.models.MovieDetailsPresentationEntity
import com.example.simplemovieapp.utilities.Formatters
import com.example.simplemovieapp.utilities.Formatters.buildImageUri
import com.example.simplemovieapp.utilities.ResourceState
import com.example.simplemovieapp.utilities.W1280
import com.example.simplemovieapp.utilities.W185
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_display_movie_details) {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var binding: FragmentDisplayMovieDetailsBinding
    private lateinit var movie: MovieDetailsPresentationEntity

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDisplayMovieDetailsBinding.bind(view)

        movieDetailsViewModel.getMovieDetails(args.id)

        subscribeToObservables()
        updateSaveButton()
        setOnClickListeners()
    }

    private fun updateScreen(result: ResourceState<MovieDetailsPresentationEntity>) {
        when (result) {
            is ResourceState.Success -> {
                movie = result.data!!
                bindViews(result.data)
            }
            is ResourceState.Error -> {

            }
            else -> Unit
        }
    }

    private fun subscribeToObservables() {
        movieDetailsViewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            updateScreen(movieDetails)
        }
        movieDetailsViewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            setSaveButton(isSaved.data!!)
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

            glide.load(buildImageUri(W1280, movieDetails.backdropPath))
                .into(imageviewMovieBackdrop)

            glide.load(buildImageUri(W185, movieDetails.backdropPath))
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