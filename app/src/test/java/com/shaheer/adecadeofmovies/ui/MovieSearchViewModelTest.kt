package com.shaheer.adecadeofmovies.ui

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.factory.MovieFactory
import com.shaheer.adecadeofmovies.getOrAwaitValue
import com.shaheer.adecadeofmovies.ui.mapper.MovieListMapper
import com.shaheer.adecadeofmovies.ui.mapper.MovieSearchListMapper
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.movies.MoviesViewModel
import com.shaheer.adecadeofmovies.ui.moviesearch.MovieSearchViewModel
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.internal.stubbing.answers.AnswersWithDelay
import org.mockito.internal.stubbing.answers.Returns
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class MovieSearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val moviesRepository = mock(MoviesRepository::class.java)
    val movieSearchListMapper = mock(MovieSearchListMapper::class.java)

    private lateinit var movieSearchViewModel: MovieSearchViewModel

    @Before
    fun initTest(){
        movieSearchViewModel = MovieSearchViewModel(moviesRepository, movieSearchListMapper)
    }

    @Test
    fun testSearchMoviesSuccess(){
        val movies = emptyList<Movie>()
        `when`(moviesRepository.getMoviesAgainstQuery("")).thenReturn(Single.fromCallable { movies })

        val testMappedMovies2019 = (1..2).map { MovieFactory.createMovieListItem(year = 2019) }.sortedByDescending { it.movie?.rating }
        val testMappedMovies2018 = (1..3).map { MovieFactory.createMovieListItem(year = 2018) }.sortedByDescending { it.movie?.rating }
        val testMappedMovies2013 = (1..4).map { MovieFactory.createMovieListItem(year = 2013) }.sortedByDescending { it.movie?.rating }

        val testMappedMovies = listOf(
            listOf(MovieListItem(MovieListItemType.Year, 2019, null)),
            testMappedMovies2019,
            listOf(MovieListItem(MovieListItemType.Year, 2018, null)),
            testMappedMovies2018,
            listOf(MovieListItem(MovieListItemType.Year, 2013, null)),
            testMappedMovies2013
        ).flatten()

        `when`(movieSearchListMapper.mapToLocal(movies)).thenReturn(testMappedMovies)

        Assert.assertEquals(
            Result.Success(testMappedMovies)
            , movieSearchViewModel.movies.getOrAwaitValue(
                latchCountDown = 2
                , afterObserve = {movieSearchViewModel.searchMovies("")}
            )
        )

        Assert.assertEquals(
            testMappedMovies2019.first().movie,
            movieSearchViewModel.movie.getOrAwaitValue(
                afterObserve = {movieSearchViewModel.getFirstMovieToDisplay()}
            )
        )
    }
}