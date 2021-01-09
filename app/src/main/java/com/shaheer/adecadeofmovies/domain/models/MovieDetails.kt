package com.shaheer.adecadeofmovies.domain.models

data class MovieDetails(
    val title: String,
    val year: Int,
    val cast: List<String>,
    val genres: List<String>,
    val rating: Int
)
