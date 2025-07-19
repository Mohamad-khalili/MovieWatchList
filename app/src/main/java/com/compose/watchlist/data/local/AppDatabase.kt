package com.compose.watchlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compose.watchlist.data.local.entities.MovieEntity

@Database(entities = [MovieEntity::class] , version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun movieDao() : MovieDao
}