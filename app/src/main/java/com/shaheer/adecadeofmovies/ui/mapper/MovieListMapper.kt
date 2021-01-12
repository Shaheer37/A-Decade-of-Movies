package com.shaheer.adecadeofmovies.ui.mapper

import com.shaheer.adecadeofmovies.domain.Mapper
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import javax.inject.Inject

class MovieListMapper @Inject constructor(): Mapper<List<MovieListItem>, List<Movie>> {
    override fun mapToLocal(remote: List<Movie>): List<MovieListItem> {
        return remote.map { movie -> MovieListItem(MovieListItemType.Movie, movie.year, movie) }
    }

    override fun mapToRemote(local: List<MovieListItem>): List<Movie> {
        TODO("Not yet implemented")
    }
}