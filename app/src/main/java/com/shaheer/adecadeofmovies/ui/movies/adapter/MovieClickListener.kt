package com.shaheer.adecadeofmovies.ui.movies.adapter

import com.shaheer.adecadeofmovies.domain.models.Movie

interface MovieClickListener {
    fun onMovieClicked(movie: Movie)
}