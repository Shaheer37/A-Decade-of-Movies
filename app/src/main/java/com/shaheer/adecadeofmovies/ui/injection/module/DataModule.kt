package com.shaheer.adecadeofmovies.ui.injection.module

import android.content.Context
import com.shaheer.adecadeofmovies.BuildConfig
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import com.shaheer.adecadeofmovies.data.retrofit.DataFactory
import com.shaheer.adecadeofmovies.data.retrofit.MoviesService
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

    }

//    @Binds
//    @Singleton
//    fun bindsMoviesRepository(moviesRepository: MoviesRepositoryImpl): MoviesRepository
}