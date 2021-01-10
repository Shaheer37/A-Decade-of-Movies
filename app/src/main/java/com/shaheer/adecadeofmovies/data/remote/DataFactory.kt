package com.shaheer.adecadeofmovies.data.remote

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shaheer.adecadeofmovies.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object DataFactory{

    fun makeService(baseUrl: HttpUrl = BuildConfig.FlickrApiBaseUrl.toHttpUrl(), context: Context, showLogs: Boolean = true): MoviesService {
        val okHttpClient = makeOkHttpClient(
                makeNetworkConnectivityInterceptor(context),
                makeLoggingInterceptor(showLogs)
        )
        return makeService(baseUrl, okHttpClient)
    }

    private fun makeService(baseUrl: HttpUrl, okHttpClient: OkHttpClient): MoviesService {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(MoviesService::class.java)
    }

    private fun makeOkHttpClient(
        networkConnectivityInterceptor: NetworkConnectionInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkConnectivityInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private fun getGson(): Gson{
        return GsonBuilder()
            .create()
    }

    private fun makeNetworkConnectivityInterceptor(context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }


    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor{
        val logging = HttpLoggingInterceptor()
        logging.level = if(isDebug){
            HttpLoggingInterceptor.Level.BODY
        } else{
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}