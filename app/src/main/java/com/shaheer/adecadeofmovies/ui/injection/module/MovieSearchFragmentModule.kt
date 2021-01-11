package com.shaheer.adecadeofmovies.ui.injection.module

import com.shaheer.adecadeofmovies.ui.injection.qualifiers.MovieSearchFragmentQualifier
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesAdapter
import com.shaheer.adecadeofmovies.ui.moviesearch.MovieSearchFragment
import dagger.Module
import dagger.Provides

@Module
abstract class MovieSearchFragmentModule {

    @Provides
    @MovieSearchFragmentQualifier
    fun bindsMoviesAdapter(movieClickListener: MovieSearchFragment): MoviesAdapter {
        return MoviesAdapter(movieClickListener)
    }
}