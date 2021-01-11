package com.shaheer.adecadeofmovies.ui.movies

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.ui.base.BaseFragment
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.MoviesFragmentQualifier
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.moviedetail.MoviesDetailFragment
import com.shaheer.adecadeofmovies.ui.movieadapter.MovieClickListener
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesAdapter
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesItemSpacingDecoration
import com.shaheer.adecadeofmovies.utils.hasFragments
import com.shaheer.adecadeofmovies.utils.hideKeyboard
import com.shaheer.adecadeofmovies.utils.replaceFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject


class MoviesFragment : BaseFragment(), MovieClickListener {

    @Inject lateinit var viewModel: MoviesViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject
    @MoviesFragmentQualifier
    lateinit var adapter: MoviesAdapter

    private val isMasterDetail: Boolean by lazy { resources.getBoolean(R.bool.is_md) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movies, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSearch()

        rv_movies.layoutManager = LinearLayoutManager(requireContext())
        rv_movies.adapter = adapter
        rv_movies.addItemDecoration(MoviesItemSpacingDecoration())

        viewModel.movies.observe(viewLifecycleOwner, Observer { handleMoviesResult(it) })
        viewModel.movie.observe(viewLifecycleOwner, Observer { onMovieClicked(it) })
        viewModel.getMovies()
    }

    private fun handleMoviesResult(result: Result<List<MovieListItem>>) = when(result){
        is Result.Success -> { showMovies(result.data) }
        is Result.Error -> { result.exception.printStackTrace()}
        is Result.Loading -> {}
    }

    private fun showMovies(movieItems: List<MovieListItem>){
        adapter.submitList(movieItems){
            showFirstItemIfMasterDetail()
        }
    }

    private fun showFirstItemIfMasterDetail(){
        if(isMasterDetail && !childFragmentManager.hasFragments()){
            viewModel.getFirstMovieToDisplay()
        }
    }

    override fun onMovieClicked(movie: Movie) {
        requireActivity().currentFocus?.hideKeyboard()
        if(isMasterDetail) replaceFragment(MoviesDetailFragment.newInstance(movie.id), R.id.fragment_container)
        else findNavController().navigate(MoviesFragmentDirections.actionMoviesToMoviesDetail(movie.id))
    }

    private fun setUpSearch(){
        toolbar.inflateMenu(R.menu.menu)
        toolbar.menu.findItem(R.id.action_search).setOnMenuItemClickListener {
            findNavController().navigate(MoviesFragmentDirections.actionMoviesToMovieSearch())
            true
        }
    }
}