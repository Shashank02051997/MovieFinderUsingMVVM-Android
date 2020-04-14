package com.shashank.moviefindermvvm.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(

    @SerializedName("Response")
    var response: String,

    @SerializedName("Website")
    var website: String,

    @SerializedName("Production")
    var production: String,

    @SerializedName("BoxOffice")
    var boxoffice: String,

    @SerializedName("DVD")
    var dvd: String,

    @SerializedName("Type")
    var type: String,

    @SerializedName("imdbID")
    var imdbid: String,

    @SerializedName("imdbVotes")
    var imdbvotes: String,

    @SerializedName("imdbRating")
    var imdbrating: String,

    @SerializedName("Metascore")
    var metascore: String,

    @SerializedName("Ratings")
    var ratings: List<Ratings>,

    @SerializedName("Poster")
    var poster: String,

    @SerializedName("Awards")
    var awards: String,

    @SerializedName("Country")
    var country: String,

    @SerializedName("Language")
    var language: String,

    @SerializedName("Plot")
    var plot: String,

    @SerializedName("Actors")
    var actors: String,

    @SerializedName("Writer")
    var writer: String,

    @SerializedName("Director")
    var director: String,

    @SerializedName("Genre")
    var genre: String,

    @SerializedName("Runtime")
    var runtime: String,

    @SerializedName("Released")
    var released: String,

    @SerializedName("Rated")
    var rated: String,

    @SerializedName("Year")
    var year: String,

    @SerializedName("Title")
    var title: String

) {
    data class Ratings(
        @SerializedName("Value")
        var value: String,
        @SerializedName("Source")
        var source: String
    )
}

