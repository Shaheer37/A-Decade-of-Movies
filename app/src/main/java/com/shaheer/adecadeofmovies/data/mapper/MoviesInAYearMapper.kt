package com.shaheer.adecadeofmovies.data.mapper

import com.shaheer.adecadeofmovies.data.local.models.MovieEntitiesInAYear
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import javax.inject.Inject

class MoviesInAYearMapper @Inject constructor(
    private val movieMapper: MovieMapper
): Mapper<MovieEntitiesInAYear, MoviesInAYear> {
    override fun mapToLocal(remote: MoviesInAYear): MovieEntitiesInAYear {
        TODO("Not yet implemented")
    }

    override fun mapToRemote(local: MovieEntitiesInAYear): MoviesInAYear {
        return MoviesInAYear(
            local.year,
            local.movies.map { movieMapper.mapToRemote(it) }
        )
    }
}