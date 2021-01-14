package com.shaheer.adecadeofmovies.ui.moviedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.PhotoListItem
import com.shaheer.adecadeofmovies.ui.models.PhotoListItemType
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_error.*
import kotlinx.android.synthetic.main.row_photo.*
import javax.inject.Inject

class PhotoAdapter @Inject constructor(
    private val listener: PhotoListClickListener
): ListAdapter<PhotoListItem, PhotoAdapter.PhotoListViewHolder>(PhotoDiffCallback()) {
    companion object{
        const val TYPE_PHOTO = 1
        const val TYPE_ERROR = 2
        const val TYPE_LOADING = 3

        const val SPAN_SIZE_DEFAULT = 1
        const val SPAN_SIZE_FULL_WIDTH = 2
    }

    private val spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val viewType = getItem(position)?.type?: return SPAN_SIZE_DEFAULT
            return when (viewType) {
                PhotoListItemType.Message, PhotoListItemType.Loading -> SPAN_SIZE_FULL_WIDTH
                else -> SPAN_SIZE_DEFAULT
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return when(getItem(position).type){
            PhotoListItemType.Photo -> TYPE_PHOTO
            PhotoListItemType.Loading -> TYPE_LOADING
            else -> TYPE_ERROR
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            TYPE_PHOTO -> PhotoViewHolder(inflater.inflate(R.layout.row_photo, parent, false))
            TYPE_LOADING -> LoadingItemViewHolder(inflater.inflate(R.layout.row_loading, parent, false))
            else -> MessageViewHolder(inflater.inflate(R.layout.row_error, parent, false))
        }
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = spanSizeLookup
    }

    abstract class PhotoListViewHolder(
        override val containerView: View
        ): RecyclerView.ViewHolder(containerView), LayoutContainer {
        abstract fun bind(item: PhotoListItem)
    }

    class PhotoViewHolder(containerView: View): PhotoListViewHolder(containerView){
        override fun bind(item: PhotoListItem) {
            item.photo?.let {
                Glide.with(itemView)
                    .load(it)
                    .centerCrop()
                    .into(iv_movie);
            }
        }
    }

    inner class MessageViewHolder(containerView: View): PhotoListViewHolder(containerView){
        init {
            btn_try_again.setOnClickListener { listener.onRetry() }
        }
        override fun bind(item: PhotoListItem) {
            tv_message.text = itemView.context.getString(item.message?:R.string.error)
        }
    }

    class LoadingItemViewHolder(containerView: View): PhotoListViewHolder(containerView){
        override fun bind(item: PhotoListItem) {}
    }
}

class PhotoDiffCallback : DiffUtil.ItemCallback<PhotoListItem>() {
    override fun areItemsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean {
        return oldItem.type == newItem.type
                && oldItem.message == newItem.message
                && oldItem.photo == newItem.photo
    }

    override fun areContentsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean {
        return false
    }
}