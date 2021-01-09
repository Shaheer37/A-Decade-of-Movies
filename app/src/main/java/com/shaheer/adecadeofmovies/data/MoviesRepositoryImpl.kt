package com.shaheer.adecadeofmovies.data


import com.shaheer.adecadeofmovies.data.getmovies.GetMovies
import com.shaheer.adecadeofmovies.data.local.MoviesDatabase
import com.shaheer.adecadeofmovies.data.local.entities.MovieWithActorsAndGenres
import com.shaheer.adecadeofmovies.data.mapper.MovieMapper
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val database: MoviesDatabase,
    private val getMovies: GetMovies,
    private val movieMapper: MovieMapper
): MoviesRepository {
    override fun getMovies(): Single<List<Movie>> {
        return database.moviesDao().getMovies()
            .flatMap { movieEntities ->
                if(movieEntities.isEmpty()){
                    getMovies.get()
                    .flatMap { movies ->
                        Completable.create {
                            database.moviesDao().insertMovies(movies.movies)
                            it.onComplete()
                        }
                        .andThen(database.moviesDao().getMovies())
                        .map{ it.map {movieEntity ->  movieMapper.mapToRemote(movieEntity) } }
                    }
                }else{
                    Single.just(movieEntities.map {movieEntity ->  movieMapper.mapToRemote(movieEntity) })
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}