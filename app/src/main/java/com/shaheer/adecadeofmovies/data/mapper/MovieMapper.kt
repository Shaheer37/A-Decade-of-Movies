package com.shaheer.adecadeofmovies.data.mapper

import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.domain.models.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor(): Mapper<MovieEntity, Movie> {
    override fun mapToLocal(remote: Movie): MovieEntity {
        TODO("Not yet implemented")
    }

    override fun mapToRemote(local: MovieEntity): Movie {
        return Movie(
            local.id,
            local.title,
            local.year,
            local.rating
        )
    }
}