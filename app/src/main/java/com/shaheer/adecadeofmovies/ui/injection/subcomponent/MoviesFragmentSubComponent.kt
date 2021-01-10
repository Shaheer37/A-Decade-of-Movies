package com.shaheer.adecadeofmovies.ui.injection.subcomponent

import com.shaheer.adecadeofmovies.ui.injection.module.MoviesFragmentModule
import com.shaheer.adecadeofmovies.ui.movies.MoviesFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [MoviesFragmentModule::class])
interface MoviesFragmentSubComponent: AndroidInjector<MoviesFragment> {
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<MoviesFragment>
}