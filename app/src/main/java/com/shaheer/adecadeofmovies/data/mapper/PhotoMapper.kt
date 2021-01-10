package com.shaheer.adecadeofmovies.data.mapper

import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.remote.Photo
import com.shaheer.adecadeofmovies.domain.Mapper
import com.shaheer.adecadeofmovies.domain.models.Movie
import javax.inject.Inject

class PhotoMapper @Inject constructor(): Mapper<Photo, String> {
    override fun mapToLocal(remote: String): Photo {
        TODO("Not yet implemented")
    }

    override fun mapToRemote(local: Photo): String {
        return "https://live.staticflickr.com/${local.server}/${local.id}_${local.secret}_b.jpg"
    }
}