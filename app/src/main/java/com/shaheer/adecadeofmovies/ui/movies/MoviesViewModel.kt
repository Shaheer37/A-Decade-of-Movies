package com.shaheer.adecadeofmovies.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.models.Result
import java.lang.Exception
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel() {

    private var _movies = MutableLiveData<Result<List<Movie>>>()
    val movies: LiveData<Result<List<Movie>>> = _movies

    fun getMovies(){
        val disposable = moviesRepository.getMovies()
            .subscribe { movies, throwable ->
                movies?.let { _movies.value = Result.Success(movies) }
                throwable?.let { _movies.value = Result.Error(Exception(it)) }
            }
        _movies.value = Result.Loading
        compositeDisposable.add(disposable)
    }
}