package com.shaheer.adecadeofmovies.ui.movies

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import kotlinx.android.extensions.LayoutContainer

class MoviesAdapter: ListAdapter<MovieListItem, RecyclerView.ViewHolder>(MoviesDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    abstract class ItemViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer{
        abstract fun bind()
    }

    class MovieItemViewHolder(override val containerView: View): ItemViewHolder(containerView){
        override fun bind() {

        }
    }

    class YearItemViewHolder(override val containerView: View): ItemViewHolder(containerView){
        override fun bind() {

        }
    }
}
class MoviesDiffCallback : DiffUtil.ItemCallback<MovieListItem>() {
    override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
        return oldItem.type == newItem.type
                && oldItem.year == newItem.year
                && oldItem.movie?.id == newItem.movie?.id
    }

    override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
        return oldItem.movie == newItem.movie
    }
}