package com.shaheer.adecadeofmovies.ui.injection.module

import android.content.Context
import com.shaheer.adecadeofmovies.ui.App
import com.shaheer.adecadeofmovies.ui.MainActivity
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.ApplicationContext
import com.shaheer.adecadeofmovies.ui.moviedetail.MoviesDetailFragment
import com.shaheer.adecadeofmovies.ui.movies.MoviesFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface UiModule {

    @Module
    companion object{
    }

    @Binds
    @ApplicationContext
    fun bindsContext(app: App): Context

//    @Binds
//    @IntoMap
//    @ClassKey(MoviesFragment::class)
//    fun bindMoviesFragmentAndroidInjectorFactory(factory: MoviesFragmentSubcomponent.Factory): AndroidInjector.Factory<*>

    @ContributesAndroidInjector
    fun contributesMoviesFragmentFragment(): MoviesFragment

    @ContributesAndroidInjector
    fun contributesMoviesDetailFragment(): MoviesDetailFragment

    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity

//    @Binds
//    fun bindsPostExecutionThread(postExecutionThread: UiThread): PostExecutionThread
}