// File: app/src/main/java/com/example/quranapp/data/local/FavoriteVerseEntity.kt
package com.example.quranapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_verses")
data class FavoriteVerseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val description: String? = null
)