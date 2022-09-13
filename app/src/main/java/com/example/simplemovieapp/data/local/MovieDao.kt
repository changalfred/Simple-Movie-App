package com.example.simplemovieapp.data.local

import androidx.room.*
import com.example.simplemovieapp.data.local.models.MovieCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE isSaved = 'true'")
    fun getSavedMovies(): Flow<List<MovieCacheEntity>>

    @Query("SELECT COUNT(1) FROM movies WHERE id = :id")
    fun checkIfMovieSaved(id: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieCacheEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieCacheEntity)

}