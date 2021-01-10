package com.shaheer.adecadeofmovies.ui.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.movies.adapter.MoviesAdapter
import com.shaheer.adecadeofmovies.ui.movies.adapter.MoviesItemSpacingDecoration
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject


class MoviesFragment : Fragment() {

    @Inject lateinit var viewModel: MoviesViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val adapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_movies.layoutManager = LinearLayoutManager(requireContext())
        rv_movies.adapter = adapter
        rv_movies.addItemDecoration(MoviesItemSpacingDecoration())

        viewModel.movies.observe(viewLifecycleOwner, Observer { handleMoviesResult(it) })
        viewModel.getMovies()
    }

    private fun handleMoviesResult(result: Result<List<MovieListItem>>) = when(result){
        is Result.Success -> {
            adapter.submitList(result.data)
        }
        is Result.Error -> { result.exception.printStackTrace()}
        is Result.Loading -> {}
    }
}