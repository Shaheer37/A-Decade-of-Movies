package com.shaheer.adecadeofmovies.data.local.entities

import androidx.room.*

data class MovieWithActorsAndGenres(
    @Embedded val movieEntity: MovieEntity,
    @Relation(
        parentColumn= "id",
        entityColumn = "movieId",
        entity = Actor::class
    )
    val actors: List<Actor>,
    @Relation(
        parentColumn= "id",
        entityColumn = "movieId",
        entity = Genre::class
    )
    val genre: List<Genre>
)

@Entity(indices = [Index(value = arrayOf("title")), Index(value = arrayOf("rating"))])
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val year: Int,
    val rating: Int
)