package com.example.quranapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranapp.data.model.Surah
import com.example.quranapp.data.repository.QuranRepository
import kotlinx.coroutines.launch

class SurahViewModel : ViewModel() {
    private val repository = QuranRepository()

    var surahList = mutableStateOf<List<Surah>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        fetchSurahs()
    }

    fun fetchSurahs() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.getSurahList()
                surahList.value = response.data
                errorMessage.value = null
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
            isLoading.value = false
        }
    }
}