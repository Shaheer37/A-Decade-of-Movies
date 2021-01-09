package com.shaheer.adecadeofmovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.shaheer.adecadeofmovies.data.getmovies.models.Movie
import com.shaheer.adecadeofmovies.data.local.entities.Actor
import com.shaheer.adecadeofmovies.data.local.entities.Genre
import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import io.reactivex.Single

@Dao
interface MoviesDao {
    @Transaction
    @Query("Select * from MovieEntity where year = :year")
    fun getMoviesDetailsByYear(year: Int): Single<List<MovieWithActorsAndGenres>>

    @Query("Select * from MovieEntity order by year")
    fun getMovies(): Single<List<MovieEntity>>

    @Insert fun insertMovie(movie: MovieEntity): Long

    @Insert fun insertActor(actor: Actor)

    @Insert fun insertGenre(genre: Genre)

    @Transaction
    fun insertMovies(movies: List<Movie>){
        movies.forEach {
            val movieId = insertMovie(MovieEntity(0, it.title, it.year, it.rating))
            it.cast.forEach { actor -> insertActor(Actor(0, movieId.toInt(), actor)) }
            it.genres.forEach { genre -> insertGenre(Genre(0, movieId.toInt(), genre)) }
        }
    }
}