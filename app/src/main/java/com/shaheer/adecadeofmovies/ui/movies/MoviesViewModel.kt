package com.shaheer.adecadeofmovies.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.models.MoviesInAYear
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.models.Result
import java.lang.Exception
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel() {

    private var _movies = MutableLiveData<Result<List<MoviesInAYear>>>()
    val movies: LiveData<Result<List<MoviesInAYear>>> = _movies

    fun getMovies(){
        val disposable = moviesRepository.getMovies()
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
            .subscribe { moviesInYears, throwable ->
                Log.d("MoviesViewModel","Duration :: ${System.currentTimeMillis() - startTime}")
//                moviesInYears?.let{
//                    it.forEach { moviesInAYear ->
//                        Log.d("MoviesViewModel","Year :: ${moviesInAYear.year}")
//                        moviesInAYear.movies.forEach { movie ->
//                            Log.d("MoviesViewModel","-- Movie :: $movie")
//                        }
//                    }
//                }
                throwable?.printStackTrace()
            }
        compositeDisposable.add(disposable)
    }
}