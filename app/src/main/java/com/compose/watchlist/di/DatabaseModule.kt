package com.compose.watchlist.di

import android.content.Context
import androidx.room.Room
import com.compose.watchlist.data.local.AppDatabase
import com.compose.watchlist.data.local.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext : Context) : AppDatabase{
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "movie_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: AppDatabase): MovieDao{
        return database.movieDao()
    }
}