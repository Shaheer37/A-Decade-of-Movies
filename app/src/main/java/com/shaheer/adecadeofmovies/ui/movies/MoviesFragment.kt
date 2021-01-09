package com.shaheer.adecadeofmovies.ui.movies

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import com.shaheer.adecadeofmovies.ui.models.Result
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movies.observe(viewLifecycleOwner, Observer { handleMoviesResult(it) })
        viewModel.getMovies()
    }

    private fun handleMoviesResult(result: Result<List<Movie>>) = when(result){
        is Result.Success -> {
            result.data.forEach { Log.d("MoviesFragment", it.toString()) }
        }
        is Result.Error -> { result.exception.printStackTrace()}
        is Result.Loading -> {}
    }
}