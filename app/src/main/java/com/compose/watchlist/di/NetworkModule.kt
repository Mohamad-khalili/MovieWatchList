package com.compose.watchlist.di

import com.compose.watchlist.data.remote.OmdbApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = "https://www.omdbapi.com/"


    @Provides
    @Singleton
    fun provideConverterFactory(): Factory =
        GsonConverterFactory.create(GsonBuilder().setLenient().create())


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(networkInterceptor).build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Factory, baseUrl: String):
            Retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(converterFactory).client(okHttpClient).build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): OmdbApi =
        retrofit.create(OmdbApi::class.java)
}