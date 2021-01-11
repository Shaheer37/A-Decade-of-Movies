package com.shaheer.adecadeofmovies.ui.models

import androidx.annotation.StringRes
import com.shaheer.adecadeofmovies.domain.models.Movie

data class MovieListItem(
    val type: MovieListItemType,
    val year: Int? = null,
    val movie: Movie? = null,
    @StringRes val message: Int? = null,
)
