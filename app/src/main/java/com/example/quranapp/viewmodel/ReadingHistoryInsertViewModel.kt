package com.example.quranapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranapp.data.local.QuranDatabase
import com.example.quranapp.data.local.ReadingHistoryEntity
import kotlinx.coroutines.launch

class ReadingHistoryInsertViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = QuranDatabase.getDatabase(application).quranDao()

    fun addHistory(surahNumber: Int, ayahNumber: Int, text: String) {
        viewModelScope.launch {
            val entry = ReadingHistoryEntity(
                surahNumber = surahNumber,
                ayahNumber = ayahNumber,
                text = text
            )
            dao.insertHistory(entry)
        }
    }
}