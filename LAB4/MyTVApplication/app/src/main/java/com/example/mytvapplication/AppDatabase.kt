package com.example.mytvapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_database"
                )
                .addCallback(MovieDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class MovieDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.movieDao())
                }
            }
        }

        suspend fun populateDatabase(movieDao: MovieDao) {
            val movies = mutableListOf<Movie>()
            val categories = listOf("Series", "New Movies", "Old Movies")
            val baseImgUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/"
            val posters = listOf("BigBuckBunny.jpg", "ElephantsDream.jpg", "ForBiggerBlazes.jpg", "ForBiggerEscapes.jpg", "ForBiggerFun.jpg")

            for (cat in categories) {
                for (i in 1..10) {
                    movies.add(Movie(
                        title = "$cat $i",
                        studio = "Studio $i",
                        description = "Đây là mô tả chi tiết cho phim $cat số $i.",
                        cardImageUrl = baseImgUrl + posters[i % posters.size],
                        category = cat
                    ))
                }
            }
            movieDao.insertAll(movies)
        }
    }
}
