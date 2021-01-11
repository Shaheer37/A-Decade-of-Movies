package com.shaheer.adecadeofmovies.ui.movieadapter

import com.shaheer.adecadeofmovies.domain.models.Movie

interface MovieClickListener {
    fun onMovieClicked(movie: Movie)
}