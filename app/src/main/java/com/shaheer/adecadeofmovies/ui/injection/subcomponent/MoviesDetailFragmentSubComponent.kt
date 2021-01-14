package com.shaheer.adecadeofmovies.ui.injection.subcomponent

import com.shaheer.adecadeofmovies.ui.injection.module.MoviesDetailFragmentModule
import com.shaheer.adecadeofmovies.ui.moviedetail.MoviesDetailFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [MoviesDetailFragmentModule::class])
interface MoviesDetailFragmentSubComponent: AndroidInjector<MoviesDetailFragment>{
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<MoviesDetailFragment>
}