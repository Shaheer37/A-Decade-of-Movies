package com.shaheer.adecadeofmovies.data.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shaheer.adecadeofmovies.TestThreads
import com.shaheer.adecadeofmovies.data.MoviesRepositoryImpl
import com.shaheer.adecadeofmovies.data.mapper.MovieDetailsMapper
import com.shaheer.adecadeofmovies.data.mapper.MovieMapper
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class MoviesRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var moviesRepository: MoviesRepository
    private val getMoviesData = FakeGetMoviesDataImpl()
    private lateinit var database: MoviesDatabase
    private val moviesMapper = MovieMapper()
    private val movieDetailsMapper = MovieDetailsMapper()

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java
        ).allowMainThreadQueries().build()

        moviesRepository = MoviesRepositoryImpl(
            database
            , getMoviesData
            , moviesMapper
            , movieDetailsMapper
            , TestThreads()
        )
    }

    @Test
    fun testMovieDetailRetrieval(){
        val testCast = MovieFactory.createListOfString(3)
        val testGenre = MovieFactory.createListOfString(2)
        val testMovie = MovieFactory.createMovieRemote(cast = testCast, genre = testGenre)

        database.moviesDao().insertMovies(listOf(testMovie))

        var movieId = 0
        database.moviesDao().getSortedMovies().test().assertValue {
            movieId = it.first().id
            it.size == 1
                    && it.first().title == testMovie.title
                    && it.first().rating == testMovie.rating
                    && it.first().year == testMovie.year
        }

        moviesRepository.getMovieDetails(movieId).test().assertValue {
            it.title == testMovie.title
                    && it.year == testMovie.year
                    && it.rating == testMovie.rating
                    && it.cast == testCast
                    && it.genres == testGenre
        }
    }

    @Test
    fun testGetMovies(){
        val testMovies2018 = (1..5).map{ MovieFactory.createMovieRemote(year = 2018) }
        val testMovies2017 = (1..3).map { MovieFactory.createMovieRemote(year = 2017) }
        val testMovies2015 = (1..7).map { MovieFactory.createMovieRemote(year = 2015) }

        getMoviesData.movies = listOf(testMovies2017, testMovies2015, testMovies2018).flatten()

        val sortedMovies2018 = testMovies2018.sortedByDescending { it.rating }
        val sortedMovies2017 = testMovies2017.sortedByDescending { it.rating }
        val sortedMovies2015 = testMovies2015.sortedByDescending { it.rating }

        val sortedMovies = listOf(sortedMovies2018, sortedMovies2017, sortedMovies2015).flatten()

        moviesRepository.getMovies().test().assertValue {
            it.map { it.title } == sortedMovies.map { it.title }
        }
    }

    @Test
    fun testGetSortedSearchMovies(){
        val searchString = "art"
        val testMovies2018 = (1..5).map{ MovieFactory.createMovieRemote(year = 2018, searchString = searchString) }
        val testMovies2017 = (1..3).map { MovieFactory.createMovieRemote(year = 2017, searchString = searchString) }
        val testMovies2015 = (1..7).map { MovieFactory.createMovieRemote(year = 2015, searchString = searchString) }
        val testMovies2013 = (1..9).map { MovieFactory.createMovieRemote(year = 2013, searchString = searchString) }

        val testMovies = listOf(testMovies2017, testMovies2015, testMovies2018, testMovies2013).flatten()

        val sortedMovies2018 = testMovies2018.filter { it.title.contains(searchString) }.sortedByDescending { it.rating }
        val sortedMovies2017 = testMovies2017.filter { it.title.contains(searchString) }.sortedByDescending { it.rating }
        val sortedMovies2015 = testMovies2015.filter { it.title.contains(searchString) }.sortedByDescending { it.rating }
        val sortedMovies2013 = testMovies2013.filter { it.title.contains(searchString) }.sortedByDescending { it.rating }

        val sortedMovies = listOf(sortedMovies2018, sortedMovies2017, sortedMovies2015, sortedMovies2013).flatten()

        database.moviesDao().insertMovies(testMovies)

        moviesRepository.getMoviesAgainstQuery(searchString).test().assertValue {
            it.map { it.title } == sortedMovies.map { it.title }
        }
    }

    @After
    fun closeDb() = database.close()
}