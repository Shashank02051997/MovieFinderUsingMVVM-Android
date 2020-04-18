package com.shashank.moviefindermvvm.data.repositories

import com.shashank.moviefindermvvm.data.model.MovieDetail
import com.shashank.moviefindermvvm.data.network.ApiInterface
import com.shashank.moviefindermvvm.data.network.SafeApiRequest

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