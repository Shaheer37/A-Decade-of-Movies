package com.shaheer.adecadeofmovies.domain.repositories

import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import io.reactivex.Single

interface MoviesRepository {
    fun getMovies(): Single<List<MoviesInAYear>>
    fun getMoviesAgainstQuery(query: String): Single<List<MoviesInAYear>>
}