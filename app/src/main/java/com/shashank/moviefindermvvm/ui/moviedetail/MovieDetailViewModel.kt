package com.shashank.moviefindermvvm.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashank.moviefindermvvm.data.model.MovieDetail
import com.shashank.moviefindermvvm.data.repositories.MovieDetailRepository
import com.shashank.moviefindermvvm.util.ApiException
import com.shashank.moviefindermvvm.util.AppConstant
import com.shashank.moviefindermvvm.util.NoInternetException
import com.shashank.moviefindermvvm.util.State
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val repository: MovieDetailRepository
) : ViewModel() {

    private val _movieDetailLiveData = MutableLiveData<State<MovieDetail>>()
    val movieDetailLiveData: LiveData<State<MovieDetail>>
        get() = _movieDetailLiveData
    private lateinit var movieDetailResponse: MovieDetail

    fun getMovieDetail(movieTitle: String) {
        _movieDetailLiveData.postValue(State.loading())
        viewModelScope.launch {
            try {
                movieDetailResponse = repository.getMovieDetail(movieTitle, AppConstant.API_KEY)
                _movieDetailLiveData.postValue(State.success(movieDetailResponse))
                return@launch
            } catch (e: ApiException) {
                _movieDetailLiveData.postValue(State.error(e.message!!))
            } catch (e: NoInternetException) {
                _movieDetailLiveData.postValue(State.error(e.message!!))
            }
        }
    }

}