package com.shaheer.adecadeofmovies.domain.models

import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity

data class MoviesInAYear(
    val year: Int,
    val movies: List<Movie>
)
