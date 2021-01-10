package com.shaheer.adecadeofmovies.ui.models

data class PhotoListItem(
    val type: PhotoListItemType,
    val message: String? = null,
    val photo: String? = null
)
