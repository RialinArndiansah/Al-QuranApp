package com.example.quranapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranapp.data.local.QuranDatabase
import com.example.quranapp.data.local.ReadingHistoryEntity
import com.example.quranapp.data.model.Surah
import com.example.quranapp.data.model.allSurah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReadingHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = QuranDatabase.getDatabase(application).quranDao()
    private val _historyItems = MutableStateFlow<List<ReadingHistory>>(emptyList())
    val historyItems = _historyItems.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            dao.getAllHistory().collect { entities ->
                val historyList = entities.map { entity ->
                    val surah = allSurah.find { it.number == entity.surahNumber } ?: allSurah[0]
                    val lastReadTime = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(entity.timestamp ?: System.currentTimeMillis()))
                    ReadingHistory(surah, lastReadTime, entity.ayahNumber)
                }
                _historyItems.value = historyList
            }
        }
    }

    fun searchHistory(query: String) {
        viewModelScope.launch {
            dao.searchHistory(query).collect { entities ->
                val historyList = entities.map { entity ->
                    val surah = allSurah.find { it.number == entity.surahNumber } ?: allSurah[0]
                    val lastReadTime = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(entity.timestamp ?: System.currentTimeMillis()))
                    ReadingHistory(surah, lastReadTime, entity.ayahNumber)
                }
                _historyItems.value = historyList
            }
        }
    }
}

data class ReadingHistory(
    val surah: Surah,
    val lastReadTime: String,
    val lastAyah: Int
)