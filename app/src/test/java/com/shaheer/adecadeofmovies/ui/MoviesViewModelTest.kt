package com.shaheer.adecadeofmovies.ui

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.factory.MovieFactory
import com.shaheer.adecadeofmovies.getOrAwaitValue
import com.shaheer.adecadeofmovies.ui.mapper.MovieListMapper
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.movies.MoviesViewModel
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
class MoviesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val moviesRepository = mock(MoviesRepository::class.java)
    val movieListMapper = mock(MovieListMapper::class.java)

    private lateinit var moviesViewModel: MoviesViewModel

    @Before
    fun initTest(){
        moviesViewModel = MoviesViewModel(moviesRepository, movieListMapper)
    }

    @Test
    fun testGetMoviesSuccess(){
        val movies = emptyList<Movie>()
        `when`(moviesRepository.getMovies()).thenReturn(Single.fromCallable { movies })

        val testMappedMovies2018 = (1..3).map { MovieFactory.createMovieListItem(year = 2018) }.sortedByDescending { it.movie?.rating }
        val testMappedMovies2019 = (1..2).map { MovieFactory.createMovieListItem(year = 2019) }.sortedByDescending { it.movie?.rating }
        val testMappedMovies2013 = (1..4).map { MovieFactory.createMovieListItem(year = 2013) }.sortedByDescending { it.movie?.rating }

        val testMappedMovies = listOf(
            testMappedMovies2019,
            testMappedMovies2018,
            testMappedMovies2013
        ).flatten()

        `when`(movieListMapper.mapToLocal(movies)).thenReturn(testMappedMovies)

        Assert.assertEquals(
            Result.Success(testMappedMovies)
            , moviesViewModel.movies.getOrAwaitValue(
                latchCountDown = 2
                , afterObserve = {moviesViewModel.getMovies()}
            )
        )

        Assert.assertEquals(
            testMappedMovies.first().movie,
            moviesViewModel.movie.getOrAwaitValue(
                afterObserve = {moviesViewModel.getFirstMovieToDisplay()}
            )
        )
    }
}