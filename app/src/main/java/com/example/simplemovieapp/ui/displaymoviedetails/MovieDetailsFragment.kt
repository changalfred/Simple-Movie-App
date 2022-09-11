package com.example.simplemovieapp.ui.displaymoviedetails

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
import com.example.simplemovieapp.ui.displaymoviedetails.viewmodels.MovieDetailsViewModel
import com.example.simplemovieapp.ui.displaymoviedetails.viewmodels.MovieDetailsViewModelProviderFactory
import com.example.simplemovieapp.ui.models.UiMovieDetails
import com.example.simplemovieapp.utils.Constants
import com.example.simplemovieapp.utils.Formatters
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.fragment_display_movie_details) {

    private lateinit var binding: FragmentDisplayMovieDetailsBinding
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var movie: UiMovieDetails

    private var isFavourite = false

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDisplayMovieDetailsBinding.bind(view)

        setupViewModel()

        movieDetailsViewModel.getMovieDetails(args.id, getString(R.string.tmdb_api_key), "en-US")

        subscribeToObservables()
        updateFavouriteButton()
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
        movieDetailsViewModel.isSaved.observe(viewLifecycleOwner) { favourited ->
            Timber.d("MovieDetailsFragment: observe favourited: $favourited")
//            isFavourite = favourited
            setFavouriteButton(favourited)
        }
    }

    private fun updateFavouriteButton() {
        Timber.d("MovieDetailsFragment: updateFavouriteButton()")
        isFavourited()
    }

    private fun isFavourited() {
        Timber.d("MovieDetailsFragment: isFavourited()")
        movieDetailsViewModel.checkIfFavourited(args.id)
    }

    private fun setFavouriteButton(favourited: Boolean) {
        if (!favourited) {
            Timber.d("MovieDetailsFragment: setFavouriteButton(): is NOT favourite")
            binding.imageviewFavourite.visibility = View.INVISIBLE
            binding.imageviewFavouriteBorder.visibility = View.VISIBLE
        } else {
            Timber.d("MovieDetailsFragment: setFavouriteButton(): is favourite")
            binding.imageviewFavourite.visibility = View.VISIBLE
            binding.imageviewFavouriteBorder.visibility = View.INVISIBLE
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            imageviewFavouriteBorder.setOnClickListener {
                Timber.d("MovieDetailsFragment: setOnClickListeners(): click border")
                addToFavourites()
            }
            imageviewFavourite.setOnClickListener {
                Timber.d("MovieDetailsFragment: setOnClickListeners(): click non-border")
                removeFromFavourites()
            }
            imageviewRate.setOnClickListener {

            }
            imageviewShare.setOnClickListener {

            }
        }
    }

    private fun addToFavourites() {
        Timber.d("MovieDetailsFragment: addToFavourites()")
        movieDetailsViewModel.saveMovie(movie, args.id)
        updateFavouriteButton()
    }

    private fun removeFromFavourites() {
        Timber.d("MovieDetailsFragment: removeFromFavourites()")
        movieDetailsViewModel.unsaveMovie(movie, args.id)
        updateFavouriteButton()
    }

    private fun bindViews(movieDetails: UiMovieDetails) {
        binding.apply {
            Glide.with(this@MovieDetailsFragment)
                .load(Constants.TMDB_IMAGE_BASE_URL + Constants.W500 + movieDetails.backdropPath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageviewMovieBackdrop)

            Glide.with(this@MovieDetailsFragment)
                .load(Constants.TMDB_IMAGE_BASE_URL + Constants.W185 + movieDetails.posterPath)
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