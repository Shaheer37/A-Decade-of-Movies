package com.shaheer.adecadeofmovies.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val year: Int,
    val cast: List<String>,
    val genres: List<String>,
    val rating: Int
)
