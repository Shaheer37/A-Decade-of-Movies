package com.shaheer.adecadeofmovies.data

import com.shaheer.adecadeofmovies.data.mapper.PhotoMapper
import com.shaheer.adecadeofmovies.data.remote.MoviesService
import com.shaheer.adecadeofmovies.domain.executor.ExecutionThreads
import com.shaheer.adecadeofmovies.domain.repositories.PhotoRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val moviesService: MoviesService,
    private val photoMapper: PhotoMapper,
    private val executionThreads: ExecutionThreads
): PhotoRepository {
    override fun getPhotos(query: String): Single<List<String>> {
        return moviesService.getImagesForMovie(query = query)
            .map { it.photos.photo.map { photoMapper.mapToRemote(it) } }
            .subscribeOn(executionThreads.ioScheduler)
            .observeOn(executionThreads.uiScheduler)
    }
}