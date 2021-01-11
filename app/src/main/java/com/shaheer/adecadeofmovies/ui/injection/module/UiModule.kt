package com.shaheer.adecadeofmovies.ui.injection.module

import android.content.Context
import com.shaheer.adecadeofmovies.ui.App
import com.shaheer.adecadeofmovies.ui.MainActivity
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.ApplicationContext
import com.shaheer.adecadeofmovies.ui.moviedetail.MoviesDetailFragment
import com.shaheer.adecadeofmovies.ui.movies.MoviesFragment
import com.shaheer.adecadeofmovies.ui.moviesearch.MovieSearchFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module()
interface UiModule {

    @Module
    companion object{
    }

    @Binds
    @ApplicationContext
    fun bindsContext(app: App): Context

    @ContributesAndroidInjector
    fun contributesMovieSearchFragment(): MovieSearchFragment

    @ContributesAndroidInjector
    fun contributesMoviesFragment(): MoviesFragment

    @ContributesAndroidInjector
    fun contributesMoviesDetailFragment(): MoviesDetailFragment

    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity

//    @Binds
//    fun bindsPostExecutionThread(postExecutionThread: UiThread): PostExecutionThread
}