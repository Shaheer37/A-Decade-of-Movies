package com.shaheer.adecadeofmovies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.domain.repositories.PhotoRepository
import com.shaheer.adecadeofmovies.ui.base.BaseViewModel
import com.shaheer.adecadeofmovies.ui.models.*
import java.lang.Exception
import javax.inject.Inject

class MoviesDetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val photoRepository: PhotoRepository
): BaseViewModel()  {

    private var _photos = MutableLiveData<Result<List<PhotoListItem>>>()
    val photos: LiveData<Result<List<PhotoListItem>>> = _photos

    private var _movieDetails = MutableLiveData<Result<MovieDetails>>()
    val movies: LiveData<Result<MovieDetails>> = _movieDetails

    fun getMovieDetails(movieId: Int){
        val disposable = moviesRepository.getMovieDetails(movieId)
            .subscribe(
                {
                    _movieDetails.value = Result.Success(it)
                    getPhotos()
                }, {_movieDetails.value = Result.Error(it)}
            )
        _movieDetails.value = Result.Loading()
        compositeDisposable.add(disposable)
    }

    fun getPhotos(){
        val searchQuery = (_movieDetails.value?.data as? MovieDetails)?.title ?: return
        val disposable = photoRepository.getPhotos(searchQuery)
            .map { it.map { photo -> PhotoListItem(PhotoListItemType.Photo, null, photo) } }
            .subscribe { photos, throwable ->
                photos?.let {
                    _photos.value = if(it.isNotEmpty()) Result.Success(it)
                    else Result.Success(listOf(PhotoListItem(PhotoListItemType.Message, message = R.string.no_photos_found)))
                }
                throwable?.let { _photos.value = Result.Error(it, listOf(PhotoListItem(PhotoListItemType.Message))) }
            }
        _photos.value = Result.Loading(listOf(PhotoListItem(PhotoListItemType.Loading)))
        compositeDisposable.add(disposable)
    }

}