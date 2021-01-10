package com.shaheer.adecadeofmovies.ui.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_movie.*
import kotlinx.android.synthetic.main.row_year.*
import javax.inject.Inject

class MoviesAdapter @Inject constructor(
    private val movieClickListener: MovieClickListener
): ListAdapter<MovieListItem, MoviesAdapter.ItemViewHolder>(MoviesDiffCallback()) {
    companion object{
        const val TYPE_YEAR = 1
        const val TYPE_MOVIE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).type == MovieListItemType.Year) TYPE_YEAR
        else TYPE_MOVIE
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(viewType == TYPE_YEAR) YearItemViewHolder(inflater.inflate(R.layout.row_year, parent, false))
            else MovieItemViewHolder(inflater.inflate(R.layout.row_movie, parent, false), movieClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class ItemViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer{
        abstract fun bind(item: MovieListItem)
    }

    class MovieItemViewHolder(
        override val containerView: View,
        private val movieClickListener: MovieClickListener
    ): ItemViewHolder(containerView){
        override fun bind(item: MovieListItem) {
            item.movie?.let {
                tv_movie_title. text = it.title
                rating_bar.rating = it.rating.toFloat()
            }
            itemView.setOnClickListener {
                item.movie?.let { movieClickListener.onMovieClicked(it) }
            }
        }
    }

    class YearItemViewHolder(override val containerView: View): ItemViewHolder(containerView){
        override fun bind(item: MovieListItem) {
            tv_year.text = item.year?.toString()
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