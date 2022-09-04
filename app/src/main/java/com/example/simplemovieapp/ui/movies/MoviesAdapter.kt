package com.example.simplemovieapp.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.simplemovieapp.databinding.ItemMovieBinding
import com.example.simplemovieapp.ui.models.UiMovieModel
import com.example.simplemovieapp.utils.Constants


class MoviesAdapter constructor(private val movieItemClickListener: OnMovieClickListener) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<UiMovieModel>() {
        override fun areItemsTheSame(oldItem: UiMovieModel, newItem: UiMovieModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UiMovieModel, newItem: UiMovieModel) =
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
            init {
                binding.apply {
                    root.setOnClickListener {
                        val itemPosition = adapterPosition

                        if (itemPosition != RecyclerView.NO_POSITION) {
                            val id = differ.currentList[itemPosition].id
                            val title = differ.currentList[itemPosition].title
                            movieItemClickListener.onMovieClick(id, title)
                        }
                    }
                }
            }

            fun bind(movie: UiMovieModel) {
                binding.apply {
                    Glide.with(binding.root)
                        .load(Constants.TMDB_IMAGE_BASE_URL + Constants.W185 + "/" + movie.posterPath)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.imageviewMoviePoster)

                    textviewMovieTitle.text = movie.title
                    textviewRating.text = movie.rating.toString()
                    textviewMovieOverview.text = movie.overview
                }
            }
    }

    interface OnMovieClickListener {
        fun onMovieClick(id: Int, title: String)
    }

}