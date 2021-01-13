package com.shaheer.adecadeofmovies.data.local

import com.shaheer.adecadeofmovies.data.remote.getmovies.GetMoviesData
import com.shaheer.adecadeofmovies.data.remote.getmovies.models.RemoteMovie
import io.reactivex.Single

class FakeGetMoviesDataImpl: GetMoviesData {
    var remoteMovies: List<RemoteMovie> = emptyList()

    override fun invoke(): Single<List<RemoteMovie>> {
        return Single.just(remoteMovies)
    }
}