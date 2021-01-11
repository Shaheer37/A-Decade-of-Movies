package com.shaheer.adecadeofmovies.ui.injection.subcomponent

import com.shaheer.adecadeofmovies.ui.injection.module.MovieSearchFragmentModule
import com.shaheer.adecadeofmovies.ui.moviesearch.MovieSearchFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [MovieSearchFragmentModule::class])
interface MovieSearchFragmentSubComponent: AndroidInjector<MovieSearchFragment>{
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<MovieSearchFragment>
}