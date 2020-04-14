package com.shashank.moviefindermvvm.data.repositories

import com.shashank.moviefindermvvm.data.model.MovieDetail
import com.shashank.moviefindermvvm.data.network.SafeApiRequest
import com.shashank.platform.moviefinder.ApiInterface

class MovieDetailRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun getMovieDetail(
        title: String,
        apiKey: String
    ): MovieDetail {

        return apiRequest { api.getMovieDetailData(title, apiKey) }
    }


}