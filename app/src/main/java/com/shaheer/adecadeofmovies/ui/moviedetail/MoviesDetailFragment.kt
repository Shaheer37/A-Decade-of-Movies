package com.shaheer.adecadeofmovies.ui.moviedetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.movies.MoviesViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movies_detail.*
import timber.log.Timber
import javax.inject.Inject

class MoviesDetailFragment : Fragment() {

    @Inject lateinit var viewModel: MoviesDetailViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val args: MoviesDetailFragmentArgs by navArgs()

    private val isTablet: Boolean by lazy { resources.getBoolean(R.bool.is_tablet) }

    companion object {
        @JvmStatic
        fun newInstance(movieId: Int) =
            MoviesDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("movieId", movieId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MoviesDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureLayout()

        viewModel.movies.observe(viewLifecycleOwner, Observer { handleMoviesResult(it) })
        viewModel.getMovieDetails(args.movieId)
    }

    private fun configureLayout()
        = if(isTablet) appbar.visibility = View.GONE
        else {
            appbar.visibility = View.VISIBLE
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }

    private fun handleMoviesResult(result: Result<MovieDetails>) = when(result){
        is Result.Success -> {
            Timber.d(result.data.toString())
            tv_name.text = result.data.title
        }
        is Result.Error -> { result.exception.printStackTrace()}
        is Result.Loading -> {}
    }
}