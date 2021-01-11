package com.shaheer.adecadeofmovies.ui.moviesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.mapper.MovieListMapper
import com.shaheer.adecadeofmovies.ui.mapper.MovieSearchListMapper
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MovieSearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val movieSearchListMapper: MovieSearchListMapper
) : BaseViewModel() {

    private var _movies = MutableLiveData<Result<List<MovieListItem>>>()
    val movies: LiveData<Result<List<MovieListItem>>> = _movies

    fun searchMovies(query: String = ""){
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