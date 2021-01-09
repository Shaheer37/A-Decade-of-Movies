package com.shaheer.adecadeofmovies.data


import android.util.Log
import com.shaheer.adecadeofmovies.data.getmovies.GetMovies
import com.shaheer.adecadeofmovies.data.local.MoviesDatabase
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import com.shaheer.adecadeofmovies.data.mapper.MovieMapper
import com.shaheer.adecadeofmovies.data.mapper.MoviesInAYearMapper
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val database: MoviesDatabase,
    private val getMovies: GetMovies,
    private val movieMapper: MovieMapper,
    private val moviesInAYearMapper: MoviesInAYearMapper
): MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return database.moviesDao().getMovies()
            .flatMap { movieEntities ->
                if(movieEntities.isEmpty()){
                    getMovies.get()
                    .flatMap { movies ->
                        Completable.fromCallable { database.moviesDao().insertMovies(movies.movies) }
                        .andThen(database.moviesDao().getMovies())
                        .map{ it.map {movieEntity ->  movieMapper.mapToRemote(movieEntity) } }
                    }
                }else{
                    Single.fromCallable{ movieEntities.map { movieMapper.mapToRemote(it) } }
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMoviesAgainstQuery(query: String): Single<List<MoviesInAYear>> {
        return Single.fromCallable { database.moviesDao().getMoviesInYearsForQuerySecond(query) }
            /*.map {
                it.map { entry ->
                    MoviesInAYear(entry.key, entry.value.map {
                            movieEntity ->  movieMapper.mapToRemote(movieEntity)
                    })
                }
            }*/
            .map { moviesInAYear ->
                moviesInAYear.map{movieInAYear ->  moviesInAYearMapper.mapToRemote(movieInAYear)}
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}