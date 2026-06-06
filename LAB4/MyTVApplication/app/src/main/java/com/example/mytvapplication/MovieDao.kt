package com.example.mytvapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE category = :category ORDER BY id ASC")
    suspend fun getMoviesByCategory(category: String): List<Movie>

    @Query("SELECT * FROM movies WHERE category = :category ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getMoviesByCategoryPaged(category: String, limit: Int, offset: Int): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getCount(): Int
}
