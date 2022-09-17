package com.example.simplemovieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simplemovieapp.data.local.models.MovieCacheEntity
import javax.inject.Inject

@Database(entities = [MovieCacheEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    class Callback @Inject constructor() : RoomDatabase.Callback() {

//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//        }
    }

}