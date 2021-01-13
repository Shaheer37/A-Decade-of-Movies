package com.shaheer.adecadeofmovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.shaheer.adecadeofmovies.data.remote.getmovies.models.RemoteMovie
import com.shaheer.adecadeofmovies.data.local.entities.Actor
import com.shaheer.adecadeofmovies.data.local.entities.Genre
import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

@Dao
interface MoviesDao {
    @Transaction
    @Query("Select * from MovieEntity where id = :id")
    fun getMovieDetails(id: Int): Maybe<MovieWithActorsAndGenres>

    @Query("Select * from MovieEntity order by year desc, rating desc")
    fun getSortedMovies(): Single<List<MovieEntity>>

    @Query("Select * from MovieEntity where trimmedTitle like '%'||:query||'%' order by year desc, rating desc")
    fun getMoviesForQuery(query: String): Single<List<MovieEntity>>

    @Insert fun insertMovie(movie: MovieEntity): Long

    @Insert fun insertActor(actor: Actor)

    @Insert fun insertGenre(genre: Genre)

    @Transaction
    fun insertMovies(remoteMovies: List<RemoteMovie>){
        remoteMovies.forEach {
            val movieId = insertMovie(MovieEntity(0, it.title
                , it.title.replace(" ", "").toLowerCase(Locale.ROOT)
                , it.year, it.rating))
            it.cast.forEach { actor -> insertActor(Actor(0, movieId.toInt(), actor)) }
            it.genres.forEach { genre -> insertGenre(Genre(0, movieId.toInt(), genre)) }
        }
    }
}