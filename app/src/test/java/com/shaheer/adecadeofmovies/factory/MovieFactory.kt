package com.shaheer.adecadeofmovies.factory

import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.remote.getmovies.models.RemoteMovie
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

object MovieFactory {

    fun createMovieListItem(
        year: Int? = null
        , rating: Int? = null
        , searchString: String? = null
    ): MovieListItem {
        val title = if(searchString!=null && Random.nextBoolean()){
            StringBuilder(DataFactory.randomString()).insert(nextInt(0, searchString.length), searchString)
        }else DataFactory.randomString()
        val movie = Movie(
            DataFactory.randomInt(),
            title.toString(),
            year?: DataFactory.randomYear(),
            rating?: DataFactory.randomRating()
        )
        return MovieListItem(MovieListItemType.Movie, movie.year, movie)
    }

    fun createMovieDomain(
        year: Int? = null
        , rating: Int? = null
        , searchString: String? = null
    ): Movie {
        val title = if(searchString!=null && Random.nextBoolean()){
            StringBuilder(DataFactory.randomString()).insert(nextInt(0, searchString.length), searchString)
        }else DataFactory.randomString()
        return Movie(
            DataFactory.randomInt(),
            title.toString(),
            year?: DataFactory.randomYear(),
            rating?: DataFactory.randomRating()
        )
    }

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
    ): RemoteMovie {
        val title = if(searchString!=null && Random.nextBoolean()){
            StringBuilder(DataFactory.randomString()).insert(nextInt(0, searchString.length), searchString)
        }else DataFactory.randomString()
        return RemoteMovie(
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