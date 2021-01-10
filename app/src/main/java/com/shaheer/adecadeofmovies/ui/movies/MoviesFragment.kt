package com.shaheer.adecadeofmovies.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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
import com.shaheer.adecadeofmovies.ui.movies.adapter.MovieClickListener
import com.shaheer.adecadeofmovies.ui.movies.adapter.MoviesAdapter
import com.shaheer.adecadeofmovies.ui.movies.adapter.MoviesItemSpacingDecoration
import com.shaheer.adecadeofmovies.utils.hideKeyboard
import com.shaheer.adecadeofmovies.utils.replaceFragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movies.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MoviesFragment : BaseFragment(), MovieClickListener {

    @Inject lateinit var viewModel: MoviesViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject lateinit var adapter: MoviesAdapter

    private val isTablet: Boolean by lazy { resources.getBoolean(R.bool.is_tablet) }

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
        viewModel.getMovies()
    }

    private fun handleMoviesResult(result: Result<List<MovieListItem>>) = when(result){
        is Result.Success -> { adapter.submitList(result.data) }
        is Result.Error -> { result.exception.printStackTrace()}
        is Result.Loading -> {}
    }

    override fun onMovieClicked(movie: Movie) {
        requireActivity().currentFocus?.hideKeyboard()
        if(isTablet) replaceFragment(MoviesDetailFragment.newInstance(movie.id), R.id.fragment_container)
        else findNavController().navigate(MoviesFragmentDirections.actionMoviesToMoviesDetail(movie.id))
    }

    private fun setUpSearch(){
        toolbar.inflateMenu(R.menu.menu)
        val searchView = toolbar.menu.findItem(R.id.action_search).actionView as SearchView
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
        compositeDisposable.add(disposable)
    }
}