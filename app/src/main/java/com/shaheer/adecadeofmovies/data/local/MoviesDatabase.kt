package com.shaheer.adecadeofmovies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shaheer.adecadeofmovies.data.local.entities.Actor
import com.shaheer.adecadeofmovies.data.local.entities.Genre
import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.utils.SingletonHolder

@Database(
    entities = [MovieEntity::class, Actor::class, Genre::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object: SingletonHolder<MoviesDatabase, Context>(creator = { context ->
        Room.databaseBuilder(context.applicationContext,
            MoviesDatabase::class.java, "Movies.db")
            .build()
    })

    fun testGetMoviesSorted(){

    }
}