package com.shaheer.adecadeofmovies.ui

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shaheer.adecadeofmovies.factory.MovieFactory
import com.shaheer.adecadeofmovies.ui.mapper.MovieSearchListMapper
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class MovieSearchMapperTest {
    private val movieSearchListMapper = MovieSearchListMapper()

    //testing that the sorted list of movies is actually properly mapped to list of MovieItems
    // containing year items and limited to maximum 5 movies per year
    @Test
    fun testListMapping(){
        val testMovies2018 = (1..5).map{ MovieFactory.createMovieDomain(year = 2018) }.sortedByDescending { it.rating }
        val testMovies2017 = (1..3).map { MovieFactory.createMovieDomain(year = 2017) }.sortedByDescending { it.rating }
        val testMovies2015 = (1..7).map { MovieFactory.createMovieDomain(year = 2015) }.sortedByDescending { it.rating }
        val testMovies2013 = (1..9).map { MovieFactory.createMovieDomain(year = 2013) }.sortedByDescending { it.rating }

        val testMovies = listOf(testMovies2018, testMovies2017, testMovies2015, testMovies2013).flatten()

        val expectedResult = listOf(
            listOf(MovieListItem(MovieListItemType.Year, 2018, null))
            , testMovies2018.take(5).map { MovieListItem(MovieListItemType.Movie, it.year, it) }
            , listOf(MovieListItem(MovieListItemType.Year, 2017, null))
            , testMovies2017.take(5).map { MovieListItem(MovieListItemType.Movie, it.year, it) }
            , listOf(MovieListItem(MovieListItemType.Year, 2015, null))
            , testMovies2015.take(5).map { MovieListItem(MovieListItemType.Movie, it.year, it) }
            , listOf(MovieListItem(MovieListItemType.Year, 2013, null))
            , testMovies2013.take(5).map { MovieListItem(MovieListItemType.Movie, it.year, it) }
        ).flatten()

        val actualResult = movieSearchListMapper.mapToLocal(testMovies)

        Assert.assertEquals(expectedResult, actualResult)
    }
}