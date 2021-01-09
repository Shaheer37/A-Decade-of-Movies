package com.shaheer.adecadeofmovies.data.local.models

import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity

data class MovieEntitiesInAYear(
    val year: Int,
    val movies: MutableList<MovieEntity>
)
