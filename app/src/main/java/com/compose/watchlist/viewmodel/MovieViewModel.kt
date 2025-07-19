package com.compose.watchlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.watchlist.data.local.MovieDao
import com.compose.watchlist.data.local.entities.MovieEntity
import com.compose.watchlist.data.local.entities.toMovieEntity
import com.compose.watchlist.data.repository.MovieRepository
import com.compose.watchlist.domain.model.Search
import com.compose.watchlist.domain.model.movieDetail.MovieDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val movieDao: MovieDao
) : ViewModel() {


    private val _searchResult = MutableStateFlow<List<Search>>(emptyList())
    val searchResult: StateFlow<List<Search>> = _searchResult

    private val _movieDetail = MutableStateFlow<MovieDetailResponse?>(null)
    val movieDetail: StateFlow<MovieDetailResponse?> = _movieDetail

    private val _savedMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val savedMovies: StateFlow<List<MovieEntity>> = _savedMovies

    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()

    fun getMovie(searchWord: String) {
        viewModelScope.launch {
            val res = repository.search( search = searchWord)
            res.body()?.let { response ->
                val results = response.search
                if (!results.isNullOrEmpty()) {
                    _searchResult.value = results
                } else {
                    _searchResult.value = emptyList()
                }
            }
        }
    }

    fun getMovieDetail(movieId: String) {
        viewModelScope.launch {
            val res = repository.getMovieDetail(movieId)
            res.body().let { response ->
                if (response != null) {
                    _movieDetail.value = response
                    Log.d("TAG", "getMovieDetail: ${response.Released}")
                }
            }
        }
    }


    //store
    fun storeMovie(movie: MovieDetailResponse) {
        viewModelScope.launch {
            val movieItem = movie.toMovieEntity()
            movieDao.insertMovie(movieItem)
            Log.d("TAG", "storeMovie: ${movieDao.getAllMovies()}")
        }
    }

     fun getMovies() {
        viewModelScope.launch {
            _savedMovies.value = movieDao.getAllMovies()
        }
    }


    fun checkBookmarkStatus(imdbId :String){
        viewModelScope.launch {
            movieDao.isMovieBookmarked(imdbId).collect{
                _isBookmarked.value = it
            }
        }
    }

    fun removeBookmarkFromWatchList(movieEntity: MovieEntity){
        viewModelScope.launch {
            movieDao.deleteMovie(movieEntity)
            getMovies()
        }
    }

    fun toggleBookmark(movieDetail:MovieDetailResponse){
        val movieEntity = movieDetail.toMovieEntity()
        viewModelScope.launch {
            if (_isBookmarked.value) {
                movieDao.deleteMovie(movieEntity)
            } else {
                movieDao.insertMovie(movieEntity)
            }
        }
    }


}