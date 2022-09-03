package com.example.simplemovieapp.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.simplemovieapp.databinding.ItemMovieBinding
import com.example.simplemovieapp.ui.models.Movie
import com.example.simplemovieapp.utils.Constants
import timber.log.Timber


class MoviesAdapter constructor(private val movieItemClickListener: OnMovieClickListener) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.bind(movie)
    }

    override fun getItemCount() = differ.currentList.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(movie: Movie) {
                binding.apply {
                    Timber.d("Poster path: ${Constants.TMDB_IMAGE_BASE_URL + movie.posterPath}")

                    Glide.with(binding.root)
                        .load(Constants.TMDB_IMAGE_BASE_URL + movie.posterPath)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.imageviewMoviePoster)

                    textviewMovieTitle.text = movie.title
                    textviewRating.text = movie.rating.toString()
                    textviewMovieOverview.text = movie.overview
                }
            }
    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }

}