package com.example.simplemovieapp.ui.displaymoviedetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.simplemovieapp.R
import com.example.simplemovieapp.databinding.FragmentDisplayMovieDetailsBinding
import com.example.simplemovieapp.ui.models.UiMovieDetailsModel
import com.example.simplemovieapp.utils.Constants
import com.example.simplemovieapp.utils.Formatters

class DisplayMovieDetailsFragment : Fragment(R.layout.fragment_display_movie_details) {

    private lateinit var binding: FragmentDisplayMovieDetailsBinding

    private val movieDetailsViewModel: DisplayMovieDetailsViewModel by viewModels()
    private val args: DisplayMovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDisplayMovieDetailsBinding.bind(view)

        movieDetailsViewModel.getMovieDetails(args.id, getString(R.string.tmdb_api_key), "en-US")

        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        movieDetailsViewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            bindViews(movieDetails)
        }
    }

    private fun bindViews(movieDetails: UiMovieDetailsModel) {
        binding.apply {
            Glide.with(this@DisplayMovieDetailsFragment)
                .load(Constants.TMDB_IMAGE_BASE_URL + Constants.W500 + movieDetails.backdropPath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageviewMovieBackdrop)

            Glide.with(this@DisplayMovieDetailsFragment)
                .load(Constants.TMDB_IMAGE_BASE_URL + Constants.W185 + movieDetails.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageviewMoviePoster)

            textviewRating.text = Formatters.roundToNearestTenth(movieDetails.rating).toString()
            textviewReleaseDate.text = requireContext().getString(R.string.release_date, Formatters.formatDate(movieDetails.releaseDate))
            textviewRevenue.text = requireContext().getString(R.string.revenue, Formatters.formatWithCommas(movieDetails.revenue))
            textviewMovieOverview.text = movieDetails.overview
        }
    }

}