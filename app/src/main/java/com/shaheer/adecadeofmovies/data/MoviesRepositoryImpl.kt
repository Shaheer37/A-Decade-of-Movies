package com.shaheer.adecadeofmovies.data


import android.util.Log
import com.shaheer.adecadeofmovies.data.getmovies.GetMovies
import com.shaheer.adecadeofmovies.data.local.MoviesDatabase
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import com.shaheer.adecadeofmovies.data.local.models.MovieEntitiesInAYear
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

    private fun getMoviesInYearForQuerySingle() = Single.fromCallable {
        database.moviesDao().getMoviesInYearsForQuery("")
    }

    override fun getMovies(): Single<List<MoviesInAYear>> {
        return getMoviesInYearForQuerySingle()
            .flatMap { movieEntitiesInYears ->
                if(movieEntitiesInYears.isEmpty()){
                    getMovies.get().flatMap { movies ->
                        Completable.fromCallable { database.moviesDao().insertMovies(movies.movies) }
                        .andThen(Single.fromCallable{database.moviesDao().getMoviesInYearsForQuery("")})
                        .map{ moviesInAYear ->
                            moviesInAYear.map{movieInAYear ->  moviesInAYearMapper.mapToRemote(movieInAYear)}
                        }
                    }
                }else{
                    Single.fromCallable{
                        movieEntitiesInYears.map{movieInAYear ->
                            moviesInAYearMapper.mapToRemote(movieInAYear)
                        }
                    }
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMoviesAgainstQuery(query: String): Single<List<MoviesInAYear>> {
        return Single.fromCallable { database.moviesDao().getMoviesInYearsForQuery(query) }
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