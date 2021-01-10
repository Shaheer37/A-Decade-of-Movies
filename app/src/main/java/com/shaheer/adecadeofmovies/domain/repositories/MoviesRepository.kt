package com.shaheer.adecadeofmovies.domain.repositories

import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import io.reactivex.Maybe
import io.reactivex.Single

interface MoviesRepository {
    fun getMovies(): Single<List<Movie>>
    fun getMovieDetails(movieId: Int): Maybe<MovieDetails>
    fun getMoviesAgainstQuery(query: String): Single<List<MoviesInAYear>>
}