package com.compose.watchlist.data.remote

import com.compose.watchlist.domain.model.SearchResponse
import com.compose.watchlist.domain.model.movieDetail.MovieDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    //@Query("forMobile") forMobile: Boolean = true
    suspend fun search(
        @Query("apiKey") apiKey: String,
        @Query("s") t: String
    ) : Response<SearchResponse>


    @GET("/")
    suspend fun getMovieDetail(
        @Query("apiKey") apiKey: String,
        @Query("i") i: String
    ): Response<MovieDetailResponse>
}