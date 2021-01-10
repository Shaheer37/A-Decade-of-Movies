package com.shaheer.adecadeofmovies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import java.lang.Exception
import javax.inject.Inject

class MoviesDetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
): BaseViewModel()  {

    private var _movieDetails = MutableLiveData<Result<MovieDetails>>()
    val movies: LiveData<Result<MovieDetails>> = _movieDetails

    fun getMovieDetails(movieId: Int){
        val disposable = moviesRepository.getMovieDetails(movieId)
            .subscribe(
                {_movieDetails.value = Result.Success(it)},
                {_movieDetails.value = Result.Error(Exception(it))}
            )
        _movieDetails.value = Result.Loading
        compositeDisposable.add(disposable)
    }

}