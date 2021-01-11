package com.shaheer.adecadeofmovies.ui.movieadapter

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
import kotlinx.android.synthetic.main.row_message.*
import kotlinx.android.synthetic.main.row_message.view.*
import kotlinx.android.synthetic.main.row_movie.*
import kotlinx.android.synthetic.main.row_year.*
import javax.inject.Inject

class MoviesAdapter @Inject constructor(
    private val movieClickListener: MovieClickListener
): ListAdapter<MovieListItem, MoviesAdapter.ItemViewHolder>(MoviesDiffCallback()) {
    companion object{
        const val TYPE_YEAR = 1
        const val TYPE_MOVIE = 2
        const val TYPE_MESSAGE = 3
        const val TYPE_LOADING = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).type){
            MovieListItemType.Year -> TYPE_YEAR
            MovieListItemType.Movie -> TYPE_MOVIE
            MovieListItemType.Loading -> TYPE_LOADING
            else -> TYPE_MESSAGE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            TYPE_YEAR -> YearItemViewHolder(inflater.inflate(R.layout.row_year, parent, false))
            TYPE_MOVIE -> MovieItemViewHolder(inflater.inflate(R.layout.row_movie, parent, false), movieClickListener)
            TYPE_LOADING -> LoadingItemViewHolder(inflater.inflate(R.layout.row_loading, parent, false))
            else -> MessageItemViewHolder(inflater.inflate(R.layout.row_message, parent, false))
        }
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

    class MessageItemViewHolder(override val containerView: View): ItemViewHolder(containerView){
        override fun bind(item: MovieListItem) {
            tv_message.text = itemView.context.getString(item.message?:R.string.error)
        }
    }
    class LoadingItemViewHolder(override val containerView: View): ItemViewHolder(containerView){
        override fun bind(item: MovieListItem) {}
    }
}
class MoviesDiffCallback : DiffUtil.ItemCallback<MovieListItem>() {
    override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
        return oldItem.type == newItem.type
                && oldItem.year == newItem.year
                && oldItem.movie?.id == newItem.movie?.id
                && oldItem.message == newItem.message
    }

    override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
        return oldItem.movie == newItem.movie
    }
}