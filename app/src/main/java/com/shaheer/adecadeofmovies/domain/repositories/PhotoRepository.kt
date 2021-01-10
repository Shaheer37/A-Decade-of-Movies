package com.shaheer.adecadeofmovies.domain.repositories

import io.reactivex.Single

interface PhotoRepository {
    fun getPhotos(query: String): Single<List<String>>
}