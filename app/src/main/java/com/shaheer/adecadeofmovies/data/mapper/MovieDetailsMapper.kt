package com.shaheer.adecadeofmovies.data.mapper

import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import com.shaheer.adecadeofmovies.domain.Mapper
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import javax.inject.Inject

class MovieDetailsMapper @Inject constructor(): Mapper<MovieWithActorsAndGenres, MovieDetails> {
    override fun mapToLocal(remote: MovieDetails): MovieWithActorsAndGenres {
        TODO("Not yet implemented")
    }

    override fun mapToRemote(local: MovieWithActorsAndGenres): MovieDetails {
        return MovieDetails(
            local.movieEntity.id,
            local.movieEntity.title,
            local.movieEntity.year,
            local.actors.map { it.name },
            local.genre.map { it.name },
            local.movieEntity.rating
        )
    }
}