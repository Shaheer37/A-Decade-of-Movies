package com.shaheer.adecadeofmovies.data.local

import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.remote.getmovies.models.Movie
import com.shaheer.adecadeofmovies.factory.DataFactory
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

object MovieFactory {

    fun createMovieEntity(year: Int? = null, rating: Int? = null): MovieEntity{
        return MovieEntity(
            0,
            DataFactory.randomString(),
            DataFactory.randomString().replace(" ", "").toLowerCase(Locale.ROOT),
            year?: DataFactory.randomYear(),
            rating?: DataFactory.randomRating()
        )
    }

    fun createMovieRemote(
        year: Int? = null
        , rating: Int? = null
        , cast: List<String> = emptyList()
        , genre: List<String> = emptyList()
        , searchString: String? = null
    ): Movie {
        val title = if(searchString!=null && Random.nextBoolean()){
            StringBuilder(DataFactory.randomString()).insert(nextInt(0, searchString.length), searchString)
        }else DataFactory.randomString()
        return Movie(
            title.toString(),
            year?: DataFactory.randomYear(),
            cast, genre,
            rating?: DataFactory.randomRating()
        )
    }

    fun createListOfString(amount: Int? = null): List<String>{
        val upperLimit = amount?: ThreadLocalRandom.current().nextInt(1, 10 + 1)
        val list = mutableListOf<String>()
        for(i in 1..upperLimit){
            list += DataFactory.randomString()
        }
        return list
    }
}