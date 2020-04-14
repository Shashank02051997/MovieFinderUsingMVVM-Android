package com.shashank.moviefindermvvm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashank.moviefindermvvm.data.model.SearchResults
import com.shashank.moviefindermvvm.data.repositories.HomeRepository
import com.shashank.moviefindermvvm.util.ApiException
import com.shashank.moviefindermvvm.util.AppConstant
import com.shashank.moviefindermvvm.util.NoInternetException
import com.shashank.moviefindermvvm.util.State
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {


    private var movieName = ""
    private var isLoading = false
    private var pageIndex = 0
    private var totalMovies = 0

    private val _moviesLiveData = MutableLiveData<State<SearchResults>>()
    val moviesLiveData: LiveData<State<SearchResults>>
        get() = _moviesLiveData

    private val _movieNameLiveData = MutableLiveData<String>()
    val movieNameLiveData: LiveData<String>
        get() = _movieNameLiveData

    private val _pageIndexLiveData = MutableLiveData<Int>()
    val pageIndexLiveData: LiveData<Int>
        get() = _pageIndexLiveData

    private lateinit var movieResponse: SearchResults

    fun getMovies() {
        if (pageIndex == 1)
            _moviesLiveData.postValue(State.loading())
        viewModelScope.launch {
            try {
                movieResponse = repository.getMovies(movieName, AppConstant.API_KEY, pageIndex)
                if (movieResponse.response == AppConstant.SUCCESS)
                    _moviesLiveData.postValue(State.success(movieResponse))
                else
                    _moviesLiveData.postValue(State.error(movieResponse.error))
                return@launch
            } catch (e: ApiException) {
                _moviesLiveData.postValue(State.error(e.message!!))
            } catch (e: NoInternetException) {
                _moviesLiveData.postValue(State.error(e.message!!))
            }
        }
    }

    fun searchMovie(query: String) {
        movieName = query
        pageIndex = 1
        getMovies()
    }


}