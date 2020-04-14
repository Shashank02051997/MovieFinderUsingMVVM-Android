package com.shashank.moviefindermvvm.data.repositories

import com.shashank.moviefindermvvm.data.model.SearchResults
import com.shashank.moviefindermvvm.data.network.SafeApiRequest
import com.shashank.platform.moviefinder.ApiInterface

class HomeRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun getMovies(
        searchTitle: String,
        apiKey: String,
        pageIndex: Int
    ): SearchResults {

        return apiRequest { api.getSearchResultData(searchTitle, apiKey, pageIndex) }
    }


}