package com.example.quranapp.data.repository

import androidx.compose.runtime.mutableStateListOf

data class ReadingHistoryEntry(val surahNumber: Int, val ayahNumber: Int, val text: String)

object ReadingHistoryRepository {
    val history = mutableStateListOf<ReadingHistoryEntry>()

    fun addEntry(surahNumber: Int, ayahNumber: Int, text: String) {
        history.add(ReadingHistoryEntry(surahNumber, ayahNumber, text))
    }
}