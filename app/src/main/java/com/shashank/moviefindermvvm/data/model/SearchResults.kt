package com.shashank.moviefindermvvm.data.model

import com.google.gson.annotations.SerializedName

data class SearchResults(

    @SerializedName("Response")
    var response: String,

    @SerializedName("Error")
    var error: String,

    @SerializedName("totalResults")
    var totalResults: String,

    @SerializedName("Search")
    var search: ArrayList<SearchItem?>

) {
    data class SearchItem(
        @SerializedName("Type")
        var type: String,

        @SerializedName("Year")
        var year: String,

        @SerializedName("imdbID")
        var imdbID: String,

        @SerializedName("Poster")
        var poster: String,

        @SerializedName("Title")
        var title: String
    )
}
