package com.compose.watchlist.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResponse (
    @SerializedName("Search")
    val search: List<Search>,
    val totalResults : String,
    @SerializedName("Response")
    val response : String
)


data class Search(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Type")
    val Type: String,
    @SerializedName("Poster")
    val Poster: String,
): Serializable
