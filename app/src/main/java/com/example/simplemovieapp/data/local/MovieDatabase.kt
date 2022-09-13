package com.example.simplemovieapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simplemovieapp.data.local.models.MovieCacheEntity

@Database(entities = [MovieCacheEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        @Volatile
        private var dbInstance: MovieDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = dbInstance ?: synchronized(LOCK) {
            dbInstance ?: createDatabase(context).also {
                dbInstance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_db.db"
        ).build()
    }

}