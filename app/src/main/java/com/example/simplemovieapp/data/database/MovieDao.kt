package com.example.simplemovieapp.data.database

import androidx.room.*
import com.example.simplemovieapp.data.database.models.DbMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE isSaved = 'true'")
    fun getSavedMovies(): Flow<List<DbMovie>>

    @Query("SELECT COUNT(1) FROM movies WHERE id = :id")
    fun getSavedMovieIfExists(id: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: DbMovie)

    @Delete
    suspend fun deleteMovie(movie: DbMovie)

}