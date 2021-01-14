package com.shaheer.adecadeofmovies.domain.repositories

import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import io.reactivex.Maybe
import io.reactivex.Single

interface MoviesRepository {
    //returns a list of all the movies sorted on year and rating.
    fun getMovies(): Single<List<Movie>>

    //returns a list of all the movies that contain the search string sorted on year and rating.
    fun getMoviesAgainstQuery(query: String): Single<List<Movie>>

    //returns complete details of the movie with the queried id.
    fun getMovieDetails(movieId: Int): Maybe<MovieDetails>
}