package com.shaheer.adecadeofmovies.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.mapper.MovieListMapper
import com.shaheer.adecadeofmovies.ui.mapper.MovieSearchListMapper
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.movies.adapter.MovieClickListener
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val movieListMapper: MovieListMapper,
    private val movieSearchListMapper: MovieSearchListMapper
) : BaseViewModel() {

    private var _movies = MutableLiveData<Result<List<MovieListItem>>>()
    val movies: LiveData<Result<List<MovieListItem>>> = _movies

    fun getMovies(){
        val disposable = moviesRepository.getMovies()
            .map { movieListMapper.mapToLocal(it) }
            .subscribe { movies, throwable ->
                movies?.let { _movies.value = Result.Success(movies) }
                throwable?.let { _movies.value = Result.Error(Exception(it)) }
            }
        _movies.value = Result.Loading
        compositeDisposable.add(disposable)
    }

    fun searchMovies(query: String){
        val startTime = System.currentTimeMillis()
        val disposable = moviesRepository.getMoviesAgainstQuery(query)
            .map { movieSearchListMapper.mapToLocal(it) }
            .subscribe { movies, throwable ->
                Timber.d("Duration :: ${System.currentTimeMillis() - startTime}")
                movies?.let { _movies.value = Result.Success(movies) }
                throwable?.let { _movies.value = Result.Error(Exception(it)) }
            }
        compositeDisposable.add(disposable)
    }
}