package com.compose.watchlist.data.repository

import com.compose.watchlist.BuildConfig
import com.compose.watchlist.data.remote.OmdbApi
import com.compose.watchlist.domain.model.SearchResponse
import com.compose.watchlist.domain.model.movieDetail.MovieDetailResponse
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: OmdbApi
){
    suspend fun search(search: String) : Response<SearchResponse> {
        return api.search(apiKey = BuildConfig.API_KEY,search)
    }

    suspend fun getMovieDetail(movieId : String) : Response<MovieDetailResponse>{
        return api.getMovieDetail(apiKey = BuildConfig.API_KEY ,i = movieId)
    }
}