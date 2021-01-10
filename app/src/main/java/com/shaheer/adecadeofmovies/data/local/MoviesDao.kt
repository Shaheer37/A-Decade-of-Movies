package com.shaheer.adecadeofmovies.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.shaheer.adecadeofmovies.data.getmovies.models.Movie
import com.shaheer.adecadeofmovies.data.local.entities.Actor
import com.shaheer.adecadeofmovies.data.local.entities.Genre
import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import com.shaheer.adecadeofmovies.data.local.models.MovieEntitiesInAYear
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

@Dao
interface MoviesDao {
    @Transaction
    @Query("Select * from MovieEntity where id = :id")
    fun getMovieDetails(id: Int): Maybe<MovieWithActorsAndGenres>

    @Query("Select * from MovieEntity order by year desc")
    fun getMovies(): Single<List<MovieEntity>>

    @Query("Select distinct year from MovieEntity order by year desc")
    fun getDistinctYears(): List<Int>

    @Query("Select * from MovieEntity where year = :year and trimmedTitle like '%'||:query||'%' order by rating desc limit 5")
    fun getMoviesByYearForQuery(year: Int, query: String): List<MovieEntity>

    @Query("Select * from MovieEntity where trimmedTitle like '%'||:query||'%' order by year desc, rating desc")
    fun getMoviesForQuery(query: String): List<MovieEntity>

/*    @Transaction fun getMoviesInYearsForQuery(query: String, years: List<Int>? = null): Map<Int, List<MovieEntity>>{

        var query = query.replace(" ", "").toLowerCase(Locale.ROOT)
        val yearMap = mutableMapOf<Int, List<MovieEntity>>()
        (years?:getDistinctYears()).forEach { year ->
            val movies = getMoviesByYearForQuery(year, query)
            if(movies.isNotEmpty()) yearMap[year] = movies
        }
        return yearMap
    }*/

    @Transaction
    fun getMoviesInYearsForQuery(query: String): List<MovieEntitiesInAYear>{
        var query = query.replace(" ", "").toLowerCase(Locale.ROOT)
        val movies = getMoviesForQuery(query)
        val yearList = mutableListOf<MovieEntitiesInAYear>()
        var year = 0
        var index = 0
        movies.forEach { movie ->
            when {
                year == 0 ->{
                    year = movie.year
                    yearList.add(MovieEntitiesInAYear(movie.year, mutableListOf(movie)))
                }
                year != movie.year -> {
                    year = movie.year
                    index++
                    yearList.add(MovieEntitiesInAYear(movie.year, mutableListOf(movie)))
                }
                else -> {
                    yearList[index].movies += movie
                }
            }
        }
        return yearList
    }

    @Insert fun insertMovie(movie: MovieEntity): Long

    @Insert fun insertActor(actor: Actor)

    @Insert fun insertGenre(genre: Genre)

    @Transaction
    fun insertMovies(movies: List<Movie>){
        movies.forEach {
            val movieId = insertMovie(MovieEntity(0, it.title
                , it.title.replace(" ", "").toLowerCase(Locale.ROOT)
                , it.year, it.rating))
            it.cast.forEach { actor -> insertActor(Actor(0, movieId.toInt(), actor)) }
            it.genres.forEach { genre -> insertGenre(Genre(0, movieId.toInt(), genre)) }
        }
    }
}