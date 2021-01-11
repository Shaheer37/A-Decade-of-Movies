package com.shaheer.adecadeofmovies.ui.injection.module

import android.content.Context
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.MoviesFragmentQualifier
import com.shaheer.adecadeofmovies.ui.movieadapter.MovieClickListener
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesAdapter
import com.shaheer.adecadeofmovies.ui.movies.MoviesFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface MoviesFragmentModule {
    companion object{
        @Provides
        @JvmStatic
        @MoviesFragmentQualifier
        fun bindsMoviesAdapter(movieClickListener: MoviesFragment): MoviesAdapter {
            return MoviesAdapter(movieClickListener)
        }
    }
}