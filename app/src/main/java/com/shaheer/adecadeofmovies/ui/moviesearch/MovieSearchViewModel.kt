package com.shaheer.adecadeofmovies.ui.moviesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.mapper.MovieListMapper
import com.shaheer.adecadeofmovies.ui.mapper.MovieSearchListMapper
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.MovieListItemType
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.models.data
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MovieSearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val movieSearchListMapper: MovieSearchListMapper
) : BaseViewModel() {

    private var _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private var _movies = MutableLiveData<Result<List<MovieListItem>>>()
    val movies: LiveData<Result<List<MovieListItem>>> = _movies

    fun searchMovies(query: String = ""){
        _movies.value = Result.Loading(listOf(MovieListItem(MovieListItemType.Loading)))
        val disposable = moviesRepository.getMoviesAgainstQuery(query)
            .map { movieSearchListMapper.mapToLocal(it) }
            .subscribe { movies, throwable ->
                movies?.let {
                    _movies.value = if(movies.isNotEmpty()) Result.Success(movies)
                    else Result.Success(listOf(MovieListItem(MovieListItemType.Message, message = R.string.no_movies_found)))
                }
                throwable?.let { _movies.value = Result.Error(
                    it, listOf(MovieListItem(MovieListItemType.Message)))
                }
            }
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