package com.shashank.moviefindermvvm.ui.moviedetail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shashank.moviefindermvvm.R
import com.shashank.moviefindermvvm.databinding.ActivityMovieDetailScrollingBinding
import com.shashank.moviefindermvvm.ui.home.HomeActivity
import com.shashank.moviefindermvvm.util.*
import kotlinx.android.synthetic.main.activity_movie_detail_scrolling.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MovieDetailScrollingActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private lateinit var dataBind: ActivityMovieDetailScrollingBinding
    private lateinit var viewModel: MovieDetailViewModel
    private val factory: MovieDetailViewModelFactory by instance()
    private var movieTitle = ""
    private var moviePoster = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail_scrolling)
        setSupportActionBar(toolbar)
        setupUI()
        handleNetworkChanges()
        setupViewModel()
        setupAPICall()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupUI() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra(AppConstant.INTENT_TITLE) && intent.getStringExtra(AppConstant.INTENT_TITLE) != null)
            movieTitle = intent.getStringExtra(AppConstant.INTENT_TITLE)!!
        if (intent.hasExtra(AppConstant.INTENT_POSTER) && intent.getStringExtra(AppConstant.INTENT_POSTER) != null)
            moviePoster = intent.getStringExtra(AppConstant.INTENT_POSTER)!!
        dataBind.toolbar.title = movieTitle
        Glide.with(this).load(moviePoster)
            .centerCrop()
            .thumbnail(0.5f)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(dataBind.imagePoster)

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun setupAPICall() {
        viewModel.movieDetailLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    dataBind.progressBar.show()
                    dataBind.cardViewMovieDetail.hide()
                }
                is State.Success -> {
                    dataBind.progressBar.hide()
                    dataBind.cardViewMovieDetail.show()
                    state.data.let {
                        dataBind.textYear.text = "Year: ${it.year}"
                        dataBind.textDirector.text = "Director: ${it.director}"
                        dataBind.textWriter.text = "Writer: ${it.writer}"
                        dataBind.textPlot.text = it.plot
                        if (it.ratings.isNotEmpty())
                            dataBind.textImd.text =
                                "Internet Movie Database: ${it.ratings[0].value}"
                        dataBind.textMetascore.text = "Metascore: ${it.metascore}"
                        dataBind.textImdbRating.text = "IMBD Rating: ${it.imdbrating}"
                    }
                }
                is State.Error -> {
                    dataBind.progressBar.hide()
                    dataBind.cardViewMovieDetail.hide()
                    showToast(state.message)
                }
            }
        })
        getMoviesDetail(movieTitle)
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
                if (viewModel.movieDetailLiveData.value is State.Error) {
                    getMoviesDetail(movieTitle)
                }
                dataBind.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                dataBind.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(HomeActivity.ANIMATION_DURATION)
                        .setDuration(HomeActivity.ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }

    private fun getMoviesDetail(movieTitle: String) {
        viewModel.getMovieDetail(movieTitle)
    }


}
