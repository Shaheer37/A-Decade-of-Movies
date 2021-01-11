package com.shaheer.adecadeofmovies.ui.movieadapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shaheer.adecadeofmovies.R

class MoviesItemSpacingDecoration() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if(position>=0) {
            val viewType = parent.adapter?.getItemViewType(position)

            if (viewType == MoviesAdapter.TYPE_YEAR) {
                val verticalPadding = view.resources.getDimension(R.dimen.movie_item_padding_vertical).toInt()
                outRect.top = if(position==0) verticalPadding*2 else verticalPadding
                outRect.bottom = verticalPadding
            } else {
                val verticalPadding =
                    view.resources.getDimension(R.dimen.movie_item_padding_vertical).toInt()
                val horizontalPadding =
                    view.resources.getDimension(R.dimen.movie_item_padding_horizontal).toInt()
                outRect.top = if(position==0) verticalPadding*2 else verticalPadding
                outRect.bottom = verticalPadding
                outRect.left = horizontalPadding
                outRect.right = horizontalPadding
            }
        }
    }
}