package com.shaheer.adecadeofmovies.data


import com.shaheer.adecadeofmovies.data.remote.getmovies.GetMoviesData
import com.shaheer.adecadeofmovies.data.local.MoviesDatabase
import com.shaheer.adecadeofmovies.data.mapper.MovieDetailsMapper
import com.shaheer.adecadeofmovies.data.mapper.MovieMapper
import com.shaheer.adecadeofmovies.domain.executor.ExecutionThreads
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val database: MoviesDatabase,
    private val getMoviesData: GetMoviesData,
    private val movieMapper: MovieMapper,
    private val movieDetailsMapper: MovieDetailsMapper,
    private val executionThreads: ExecutionThreads
): MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return database.moviesDao().getSortedMovies()
            .flatMap { movieEntities ->
                if(movieEntities.isEmpty()){
                    getMoviesData().flatMap {
                        Completable.fromCallable { database.moviesDao().insertMovies(it) }
                            .andThen(database.moviesDao().getSortedMovies())
                            .map { it.map { movieEntity -> movieMapper.mapToRemote(movieEntity) } }
                    }
                }else{
                    Single.fromCallable { movieEntities.map { movieMapper.mapToRemote(it) }}
                }
            }.subscribeOn(executionThreads.ioScheduler)
            .observeOn(executionThreads.uiScheduler)
    }

    override fun getMoviesAgainstQuery(query: String): Single<List<Movie>> {
        return database.moviesDao().getMoviesForQuery(query)
            .map { it.map { movieEntity -> movieMapper.mapToRemote(movieEntity) } }
            .subscribeOn(executionThreads.ioScheduler)
            .observeOn(executionThreads.uiScheduler)
    }

    override fun getMovieDetails(movieId: Int): Maybe<MovieDetails> {
        return database.moviesDao().getMovieDetails(movieId)
            .map { movieDetailsMapper.mapToRemote(it) }
            .subscribeOn(executionThreads.ioScheduler)
            .observeOn(executionThreads.uiScheduler)
    }
}