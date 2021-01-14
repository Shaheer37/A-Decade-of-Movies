package com.shaheer.adecadeofmovies.ui.moviedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.MovieDetails
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import com.shaheer.adecadeofmovies.ui.models.PhotoListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movies_detail.*
import timber.log.Timber
import javax.inject.Inject

class MoviesDetailFragment : Fragment() {

    @Inject lateinit var viewModel: MoviesDetailViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject lateinit var adapter: PhotoAdapter

    private val args: MoviesDetailFragmentArgs by navArgs()

    private val isMasterDetail: Boolean by lazy { resources.getBoolean(R.bool.is_md) }

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

        rv_photos.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner, Observer { handleMoviesResult(it) })
        viewModel.photos.observe(viewLifecycleOwner, Observer { handlePhotoResult(it)})

        viewModel.getMovieDetails(args.movieId)
    }

    private fun configureLayout()
        = if(isMasterDetail) {
            toolbar.visibility = View.GONE
            (cl_details.layoutParams as FrameLayout.LayoutParams).topMargin = 0
        }
        else {
            toolbar.visibility = View.VISIBLE
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }

    private fun handleMoviesResult(result: Result<MovieDetails>) = when(result){
        is Result.Success -> {
            Timber.d(result.data.toString())
            setMovieLayout(result.data)
        }
        is Result.Error -> { result.throwable.printStackTrace()}
        is Result.Loading -> {}
    }

    private fun handlePhotoResult(result: Result<List<PhotoListItem>>) = when(result){
        is Result.Success -> {
            Timber.d(result.data.toString())
            adapter.submitList(result.data)
        }
        is Result.Error -> {
            result.data?.let { adapter.submitList(it) }
            result.throwable.printStackTrace()
        }
        is Result.Loading -> { result.data?.let { adapter.submitList(it) } }
    }

    private fun setMovieLayout(details: MovieDetails){
        tv_name.text = details.title
        rating_bar.rating = details.rating.toFloat()
        if(details.genres.isNotEmpty()) {
            details.genres.forEach {
                val chip = Chip(requireContext())
                chip.text = it
                chip_group_genre.addView(chip)
            }
        }else {
            tv_genre.visibility = View.GONE
            chip_group_genre.visibility = View.GONE
        }
        if(details.cast.isNotEmpty()) {
            details.cast.forEach {
                val chip = Chip(requireContext())
                chip.text = it
                chip_group_cast.addView(chip)
            }
        }else {
            tv_cast.visibility = View.GONE
            chip_group_cast.visibility = View.GONE
        }
    }
}