package com.compose.watchlist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.compose.watchlist.data.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM bookmarked_movies")
    suspend fun getAllMovies() : List<MovieEntity>

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_movies WHERE imdbID = :imdbId LIMIT 1)")
    fun isMovieBookmarked(imdbId: String): Flow<Boolean>
}