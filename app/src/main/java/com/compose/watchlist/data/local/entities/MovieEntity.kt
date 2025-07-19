package com.compose.watchlist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.watchlist.domain.model.movieDetail.MovieDetailResponse

@Entity(tableName = "bookmarked_movies")
data class MovieEntity(
    @PrimaryKey()
    val imdbID: String,

    val title : String,
    val imdbRate : String,
    val plot : String,
    val genre : String,
    val poster: String,
    val runtime : String
)

fun MovieDetailResponse.toMovieEntity(): MovieEntity{
    return MovieEntity(
        imdbID = this.imdbID,
        title = this.Title,
        imdbRate = this.imdbRating,
        plot = this.Plot,
        genre = this.Genre,
        poster = this.Poster,
        runtime = this.Runtime,
    )
}
