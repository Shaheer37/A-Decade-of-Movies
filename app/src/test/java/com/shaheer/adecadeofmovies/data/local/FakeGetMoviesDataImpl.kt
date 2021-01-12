package com.shaheer.adecadeofmovies.data.local

import com.shaheer.adecadeofmovies.data.remote.getmovies.GetMoviesData
import com.shaheer.adecadeofmovies.data.remote.getmovies.models.Movie
import io.reactivex.Single

class FakeGetMoviesDataImpl: GetMoviesData {
    var movies: List<Movie> = emptyList()

    override fun invoke(): Single<List<Movie>> {
        return Single.just(movies)
    }
}