package com.shaheer.adecadeofmovies.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val movieId: Int,
    val name: String
)