package com.shaheer.adecadeofmovies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.mapper.MovieListMapper
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.models.data
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val movieListMapper: MovieListMapper
) : BaseViewModel() {

    private var _movies = MutableLiveData<Result<List<MovieListItem>>>()
    val movies: LiveData<Result<List<MovieListItem>>> = _movies

    private var _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    fun getMovies(){
        val disposable = moviesRepository.getMovies()
            .map { movieListMapper.mapToLocal(it) }
            .subscribe { movies, throwable ->
                movies?.let {
                    _movies.value = if(movies.isNotEmpty()) Result.Success(movies)
                        else Result.Success(listOf(MovieListItem(MovieListItemType.Message, message = R.string.no_movies)))
                }
                throwable?.let { _movies.value = Result.Error(it, listOf(MovieListItem(MovieListItemType.Message))) }
            }
        _movies.value = Result.Loading()
        compositeDisposable.add(disposable)
    }

    fun getFirstMovieToDisplay(){
        try {
            _movie.value = (_movies.value?.data as? List<MovieListItem>)
                ?.first { it.type == MovieListItemType.Movie }?.movie
        }catch (e: NoSuchFieldException){
            e.printStackTrace()
        }
    }
}