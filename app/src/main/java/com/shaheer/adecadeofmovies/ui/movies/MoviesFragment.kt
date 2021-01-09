package com.shaheer.adecadeofmovies.ui.movies

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class MoviesFragment : Fragment() {

    @Inject lateinit var viewModel: MoviesViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }
}