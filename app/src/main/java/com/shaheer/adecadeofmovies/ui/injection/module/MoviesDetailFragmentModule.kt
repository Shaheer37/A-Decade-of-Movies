package com.shaheer.adecadeofmovies.ui.injection.module

import com.shaheer.adecadeofmovies.ui.injection.qualifiers.MoviesFragmentQualifier
import com.shaheer.adecadeofmovies.ui.moviedetail.MoviesDetailFragment
import com.shaheer.adecadeofmovies.ui.moviedetail.PhotoAdapter
import dagger.Module
import dagger.Provides

@Module
interface MoviesDetailFragmentModule {
    companion object{
        @Provides
        @JvmStatic
        fun bindsMoviesAdapter(photoClickListener: MoviesDetailFragment): PhotoAdapter {
            return PhotoAdapter(photoClickListener)
        }
    }
}