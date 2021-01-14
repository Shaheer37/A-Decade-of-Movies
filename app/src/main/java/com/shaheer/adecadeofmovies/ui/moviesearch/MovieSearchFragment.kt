package com.shaheer.adecadeofmovies.ui.moviesearch

import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shaheer.adecadeofmovies.R
import com.shaheer.adecadeofmovies.domain.models.Movie
import com.shaheer.adecadeofmovies.ui.base.BaseFragment
import com.shaheer.adecadeofmovies.ui.injection.ViewModelFactory
import com.shaheer.adecadeofmovies.ui.injection.qualifiers.MovieSearchFragmentQualifier
import com.shaheer.adecadeofmovies.ui.models.MovieListItem
import com.shaheer.adecadeofmovies.ui.models.Result
import com.shaheer.adecadeofmovies.ui.moviedetail.MoviesDetailFragment
import com.shaheer.adecadeofmovies.ui.movieadapter.MovieClickListener
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesAdapter
import com.shaheer.adecadeofmovies.ui.movieadapter.MoviesItemSpacingDecoration
import com.shaheer.adecadeofmovies.ui.movies.MoviesFragmentDirections
import com.shaheer.adecadeofmovies.utils.hasFragments
import com.shaheer.adecadeofmovies.utils.hideKeyboard
import com.shaheer.adecadeofmovies.utils.replaceFragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_search.*
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies.rv_movies
import kotlinx.android.synthetic.main.fragment_movies.toolbar
import kotlinx.android.synthetic.main.fragment_movies_detail.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MovieSearchFragment : BaseFragment(), MovieClickListener {

    companion object{
        private  const val AWAIT_SEARCH_TIMEOUT = 500L
    }

    @Inject lateinit var viewModel: MovieSearchViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject
    @MovieSearchFragmentQualifier
    lateinit var adapter: MoviesAdapter

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
        viewModel.movie.observe(viewLifecycleOwner, Observer { it?.let { onMovieClicked(it) } })
        viewModel.searchMovies()
    }

    private fun handleMoviesResult(result: Result<List<MovieListItem>>) = when(result){
        is Result.Success -> {
            showMovies(result.data)
        }
        is Result.Error -> {
            result.data?.let { showMovies(it) }
            result.throwable.printStackTrace()
        }
        is Result.Loading -> {result.data?.let { showMovies(it) }}
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
        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO
                || actionId == EditorInfo.IME_ACTION_SEARCH) {
                requireActivity().currentFocus?.hideKeyboard()
                true
            } else false
        }
        val disposable = Observable.create<String> {
            et_search.addTextChangedListener { text: Editable? ->
                it.onNext(text?.toString()?:"")
            }
        }.debounce(AWAIT_SEARCH_TIMEOUT, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({viewModel.searchMovies(it)},{it.printStackTrace()})
        compositeDisposable.add(disposable)
    }
}