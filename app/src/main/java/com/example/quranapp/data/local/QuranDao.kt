// File: app/src/main/java/com/example/quranapp/data/local/QuranDao.kt
package com.example.quranapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {
    @Query("SELECT * FROM favorite_verses ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteVerseEntity>>

    @Query("SELECT * FROM reading_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<ReadingHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(verse: FavoriteVerseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entry: ReadingHistoryEntity)

    @Query("SELECT * FROM favorite_verses WHERE text LIKE '%' || :query || '%'")
    fun searchFavorites(query: String): Flow<List<FavoriteVerseEntity>>

    @Query("SELECT * FROM reading_history WHERE text LIKE '%' || :query || '%'")
    fun searchHistory(query: String): Flow<List<ReadingHistoryEntity>>
}