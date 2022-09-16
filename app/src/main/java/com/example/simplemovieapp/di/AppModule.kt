package com.example.simplemovieapp.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.simplemovieapp.data.MoviesRepository
import com.example.simplemovieapp.data.local.MovieDatabase
import com.example.simplemovieapp.data.remote.MoviesService
import com.example.simplemovieapp.utilities.TMDB_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlideObject(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
//            .placeholder(R.layout.placeholder_movie_image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun provideDatabaseObject(@ApplicationContext context: Context) = MovieDatabase.invoke(context)

    @Singleton
    @Provides
    fun provideApiService(): MoviesService {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val loggingClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(loggingClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MoviesService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepositoryObject(
        @ApplicationContext context: Context,
        database: MovieDatabase,
        networkService: MoviesService
    ) = MoviesRepository(context, database, networkService)

}