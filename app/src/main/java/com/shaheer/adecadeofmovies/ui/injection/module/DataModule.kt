package com.shaheer.adecadeofmovies.ui.injection.module

import android.content.Context
import com.google.gson.Gson
import com.shaheer.adecadeofmovies.BuildConfig
import com.shaheer.adecadeofmovies.data.MoviesRepositoryImpl
import com.shaheer.adecadeofmovies.data.PhotoRepositoryImpl
import com.shaheer.adecadeofmovies.data.local.MoviesDatabase
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import com.shaheer.adecadeofmovies.data.remote.DataFactory
import com.shaheer.adecadeofmovies.data.remote.MoviesService
import com.shaheer.adecadeofmovies.domain.repositories.MoviesRepository
import com.shaheer.adecadeofmovies.domain.repositories.PhotoRepository
import dagger.Binds
import javax.inject.Named
import javax.inject.Singleton

@Module
interface DataModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun providesMoviesService(@ApplicationContext context: Context): MoviesService {
            return DataFactory.makeService(context = context, showLogs = BuildConfig.DEBUG)
        }

        @Provides
        @JvmStatic
        @Singleton
        fun providesMoviesDatabase(@ApplicationContext context: Context): MoviesDatabase {
            return MoviesDatabase.getInstance(context)
        }

        @Provides
        @JvmStatic
        @Singleton
        fun providesGson(): Gson { return Gson() }

    }

    @Binds
    @Singleton
    fun bindsMoviesRepository(moviesRepository: MoviesRepositoryImpl): MoviesRepository

    @Binds
    @Singleton
    fun bindsPhotoRepository(photoRepository: PhotoRepositoryImpl): PhotoRepository
}