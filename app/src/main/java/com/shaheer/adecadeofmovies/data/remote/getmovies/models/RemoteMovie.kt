package com.shaheer.adecadeofmovies.data.remote.getmovies.models

data class RemoteMovie(
    val title: String,
    val year: Int,
    val cast: List<String>,
    val genres: List<String>,
    val rating: Int
)