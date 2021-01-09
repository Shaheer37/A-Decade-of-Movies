package com.shaheer.adecadeofmovies.data.retrofit

data class PhotosResponse(
    val stat: String,
    val photos: Photos
)

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<Photo>
)

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String
)