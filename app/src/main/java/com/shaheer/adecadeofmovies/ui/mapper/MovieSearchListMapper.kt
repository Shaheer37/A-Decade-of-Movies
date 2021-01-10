package com.shaheer.adecadeofmovies.ui.mapper

import com.shaheer.adecadeofmovies.data.local.models.MovieEntitiesInAYear
import com.shaheer.adecadeofmovies.domain.Mapper
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import javax.inject.Inject

class MovieSearchListMapper @Inject constructor(): Mapper<List<MovieListItem>, List<Movie>> {
    override fun mapToLocal(remote: List<Movie>): List<MovieListItem> {
        val movieItemList = mutableListOf<MovieListItem>()
        var year = 0
        var entries = 0
        remote.forEach { movie ->
            when {
                year != movie.year -> {
                    year = movie.year
                    entries = 1
                    movieItemList += MovieListItem(MovieListItemType.Year, movie.year, null)
                    movieItemList += MovieListItem(MovieListItemType.Movie, movie.year, movie)
                }
                else -> {
                    if(entries<5) {
                        movieItemList += MovieListItem(MovieListItemType.Movie, movie.year, movie)
                    }
                }
            }
        }
        return movieItemList
    }

    override fun mapToRemote(local: List<MovieListItem>): List<Movie> {
        TODO("Not yet implemented")
    }
}