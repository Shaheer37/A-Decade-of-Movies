package com.shaheer.adecadeofmovies.ui.models

import androidx.annotation.StringRes

data class PhotoListItem(
    val type: PhotoListItemType,
    @StringRes val message: Int? = null,
    val photo: String? = null
)
