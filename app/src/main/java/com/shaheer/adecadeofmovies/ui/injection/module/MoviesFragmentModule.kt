package com.shaheer.adecadeofmovies.ui.injection.module

import com.shaheer.adecadeofmovies.ui.movies.MoviesFragment
import com.shaheer.adecadeofmovies.ui.movies.adapter.MovieClickListener
import dagger.Binds
import dagger.Module

@Module
interface MoviesFragmentModule {
    @Binds
    fun bindsMovieClickListener(movieClickListener: MoviesFragment): MovieClickListener
}