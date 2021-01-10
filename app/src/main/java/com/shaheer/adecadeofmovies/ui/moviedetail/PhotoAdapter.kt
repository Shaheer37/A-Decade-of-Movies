package com.shaheer.adecadeofmovies.ui.moviedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.PhotoListItem
import com.shaheer.adecadeofmovies.ui.models.PhotoListItemType
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_message.*
import kotlinx.android.synthetic.main.row_photo.*
import javax.inject.Inject

class PhotoAdapter @Inject constructor(

): ListAdapter<PhotoListItem, PhotoAdapter.PhotoListViewHolder>(PhotoDiffCallback()) {
    companion object{
        const val TYPE_PHOTO = 1
        const val TYPE_ERROR = 2
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return when(getItem(position).type){
            PhotoListItemType.Photo -> TYPE_PHOTO
            else -> TYPE_ERROR
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(viewType == TYPE_PHOTO) PhotoViewHolder(inflater.inflate(R.layout.row_photo, parent, false))
        else MessageViewHolder(inflater.inflate(R.layout.row_message, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class PhotoListViewHolder(
        override val containerView: View
        ): RecyclerView.ViewHolder(containerView), LayoutContainer {
        abstract fun bind(item: PhotoListItem)
    }

    inner class PhotoViewHolder(containerView: View): PhotoListViewHolder(containerView){
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
        override fun bind(item: PhotoListItem) {
            tv_message.text = item.message?: itemView.context.getString(R.string.error)
        }
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