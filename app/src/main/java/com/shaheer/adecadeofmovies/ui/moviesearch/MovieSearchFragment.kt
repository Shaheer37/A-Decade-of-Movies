package com.shaheer.adecadeofmovies.ui.moviesearch

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
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.moviedetail.MoviesDetailFragment
import com.shaheer.adecadeofmovies.ui.movieadapter.MovieClickListener
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesAdapter
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesItemSpacingDecoration
import com.shaheer.adecadeofmovies.ui.movies.MoviesFragmentDirections
import com.shaheer.adecadeofmovies.utils.hideKeyboard
import com.shaheer.adecadeofmovies.utils.replaceFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies.toolbar
import kotlinx.android.synthetic.main.fragment_movies_detail.*
import javax.inject.Inject


class MovieSearchFragment : BaseFragment(), MovieClickListener {

    @Inject lateinit var viewModel: MovieSearchViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val adapter = MoviesAdapter(this)

    private val isMasterDetail: Boolean by lazy { resources.getBoolean(R.bool.is_md) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(MovieSearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        setUpSearch()

        rv_movies.layoutManager = LinearLayoutManager(requireContext())
        rv_movies.adapter = adapter
        rv_movies.addItemDecoration(MoviesItemSpacingDecoration())

        viewModel.movies.observe(viewLifecycleOwner, Observer { handleMoviesResult(it) })
        viewModel.searchMovies()
    }

    private fun handleMoviesResult(result: Result<List<MovieListItem>>) = when(result){
        is Result.Success -> { adapter.submitList(result.data) }
        is Result.Error -> { result.exception.printStackTrace()}
        is Result.Loading -> {}
    }

    override fun onMovieClicked(movie: Movie) {
        requireActivity().currentFocus?.hideKeyboard()
        if(isMasterDetail) replaceFragment(MoviesDetailFragment.newInstance(movie.id), R.id.fragment_container)
        else findNavController().navigate(MoviesFragmentDirections.actionMoviesToMoviesDetail(movie.id))
    }

    private fun setUpSearch(){
/*        val searchView = toolbar.menu.findItem(R.id.action_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        val disposable = Observable.create<String> {
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Timber.d("onQueryTextSubmit(query: $query")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Timber.d("onQueryTextChange(newText: $newText)")
                    it.onNext(newText?:"")
                    return true
                }
            })
        }.debounce(500, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe({viewModel.searchMovies(it)},{it.printStackTrace()})
        compositeDisposable.add(disposable)*/
    }
}