package com.shaheer.adecadeofmovies.data


import android.util.Log
import com.shaheer.adecadeofmovies.data.getmovies.GetMoviesData
import com.shaheer.adecadeofmovies.data.getmovies.models.Movies
import com.shaheer.adecadeofmovies.data.local.MoviesDatabase
import com.shaheer.adecadeofmovies.data.local.entities.MovieEntity
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import com.shaheer.adecadeofmovies.data.local.models.MovieEntitiesInAYear
import com.shaheer.adecadeofmovies.data.mapper.MovieDetailsMapper
import com.shaheer.adecadeofmovies.data.mapper.MovieMapper
import com.shaheer.adecadeofmovies.data.mapper.MoviesInAYearMapper
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val database: MoviesDatabase,
    private val getMoviesData: GetMoviesData,
    private val movieMapper: MovieMapper,
    private val moviesInAYearMapper: MoviesInAYearMapper,
    private val movieDetailsMapper: MovieDetailsMapper
): MoviesRepository {

    private fun getMoviesInYearForQuerySingle() = Single.fromCallable {
        database.moviesDao().getMoviesInYearsForQuery("")
    }

    override fun getMovies(): Single<List<Movie>> {
        return database.moviesDao().getSortedMovies()
            .flatMap { movieEntities ->
                if(movieEntities.isEmpty()){
                    getMoviesData().flatMap {
                        Completable.fromCallable { database.moviesDao().insertMovies(it.movies) }
                            .andThen(database.moviesDao().getSortedMovies())
                            .map { it.map { movieEntity -> movieMapper.mapToRemote(movieEntity) } }
                    }
                }else{
                    Single.fromCallable { movieEntities.map { movieMapper.mapToRemote(it) }}
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMoviesAgainstQuery(query: String): Single<List<MoviesInAYear>> {
        return getMoviesInYearForQuerySingle()
            .map { moviesInAYear ->
                moviesInAYear.map{movieInAYear ->  moviesInAYearMapper.mapToRemote(movieInAYear)}
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMovieDetails(movieId: Int): Maybe<MovieDetails> {
        return database.moviesDao().getMovieDetails(movieId)
            .map { movieDetailsMapper.mapToRemote(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}