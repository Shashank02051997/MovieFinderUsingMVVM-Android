package com.shashank.moviefindermvvm.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.moviefindermvvm.R
import com.shashank.moviefindermvvm.data.model.SearchResults
import com.shashank.moviefindermvvm.databinding.ActivityHomeBinding
import com.shashank.moviefindermvvm.ui.adapter.CustomAdapterMovies
import com.shashank.moviefindermvvm.ui.moviedetail.MovieDetailScrollingActivity
import com.shashank.moviefindermvvm.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeActivity : AppCompatActivity(), KodeinAware {

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }

    override val kodein by kodein()
    private lateinit var dataBind: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private val factory: HomeViewModelFactory by instance()
    private lateinit var customAdapterMovies: CustomAdapterMovies
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setupUI()
        setupViewModel()
        initializeObserver()
        handleNetworkChanges()
        setupAPICall()
        //initScrollListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        searchView.apply {
            menu.findItem(R.id.search).actionView as SearchView
            queryHint = "Search"
            isSubmitButtonEnabled = true
            onActionViewExpanded()
        }
        search(searchView)
        return true
    }

    private fun setupUI() {
        customAdapterMovies = CustomAdapterMovies()
        dataBind.recyclerViewMovies.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterMovies
            addOnItemTouchListener(RecyclerItemClickListener(
                applicationContext,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        /*val searchItem = moviesList[position]
                        val intent =
                            Intent(applicationContext, MovieDetailScrollingActivity::class.java)
                        intent.putExtra(AppConstant.INTENT_POSTER, searchItem.poster)
                        intent.putExtra(AppConstant.INTENT_TITLE, searchItem.title)
                        startActivity(intent)*/
                    }

                }))
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

    }

    private fun initializeObserver() {
        viewModel.movieNameLiveData.observe(this, Observer {
            searchView.setQuery(it, false)
        })
    }

    private fun setupAPICall() {
        viewModel.moviesLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    dataBind.recyclerViewMovies.hide()
                    dataBind.linearLayoutSearch.hide()
                    dataBind.progressBar.show()
                }
                is State.Success -> {
                    dataBind.recyclerViewMovies.show()
                    dataBind.linearLayoutSearch.hide()
                    dataBind.progressBar.hide()
                    customAdapterMovies.setData(state.data.search)
                    /*if (moviesList.isNotEmpty()) {
                        moviesList.removeAt(moviesList.size - 1)
                        val scrollPosition: Int = moviesList.size
                        customAdapterMovies.notifyItemRemoved(scrollPosition)
                        moviesList.addAll(state.data.search)
                        customAdapterMovies.notifyDataSetChanged()
                        isLoading = false
                    } else {
                        moviesList.addAll(state.data.search)
                        customAdapterMovies = CustomAdapterMovies(moviesList)
                        dataBind.recyclerViewMovies.adapter = customAdapterMovies
                        totalMovies = state.data.totalResults.toInt()
                    }*/
                }
                is State.Error -> {
                    dataBind.progressBar.hide()
                    showToast(state.message)
                }
            }
        })

    }

    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected) {
                dataBind.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                dataBind.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            } else {
                /*if (viewModel.moviesLiveData.value is State.Error || customAdapterMovies.itemCount == 0) {
                    viewModel.getMovies(searchView.query.toString(), 0)
                }*/
                dataBind.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                dataBind.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }

    private fun search(searchView: SearchView) {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                dismissKeyboard(searchView)
                searchView.clearFocus()
                viewModel.searchMovie(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    /*private fun initScrollListener() {
        dataBind.recyclerViewMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = layoutManager!!.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && (totalItemCount < totalMovies)) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isLoading = true
                        loadMore()
                    }
                }
            }

        })
    }*/

}