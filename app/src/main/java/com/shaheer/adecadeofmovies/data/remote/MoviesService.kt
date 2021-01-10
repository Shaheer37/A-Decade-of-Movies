package com.shaheer.adecadeofmovies.data.remote

import com.shaheer.adecadeofmovies.BuildConfig
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService{
    companion object {
        const val RESPONSE_PAGE_SIZE = 20
        const val START_PAGE = 1
    }

    @GET("rest/")
    fun getImagesForMovie(
        @Query("method") method: String="flickr.photos.search",
        @Query("api_key") apiKey: String=BuildConfig.FlickrApiKey,
        @Query("format") format: String="json",
        @Query("nojsoncallback") jsonCallback: Int=1,
        @Query("text") query: String,
        @Query("page") pageSize: Int = RESPONSE_PAGE_SIZE,
        @Query("page") page: Int = START_PAGE
    ): Single<PhotosResponse>
}