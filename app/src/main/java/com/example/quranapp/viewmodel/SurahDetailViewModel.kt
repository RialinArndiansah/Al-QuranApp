package com.example.quranapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quranapp.data.model.SurahDetail
import com.example.quranapp.data.repository.QuranRepository
import kotlinx.coroutines.launch

class SurahDetailViewModel(private val surahNumber: Int) : ViewModel() {
    private val repository = QuranRepository()

    var detail = mutableStateOf<SurahDetail?>(null)
        private set

    var translation = mutableStateOf<SurahDetail?>(null)
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    init {
        fetchDetailAndTranslation()
    }

    private fun fetchDetailAndTranslation() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val detailResponse = repository.getSurahDetail(surahNumber)
                detail.value = detailResponse.data

                val translationResponse = repository.getSurahTranslation(surahNumber)
                translation.value = translationResponse.data

            } catch (e: Exception) {
                errorMessage.value = e.message
            }
            isLoading.value = false
        }
    }


    @Suppress("UNCHECKED_CAST")
class SurahDetailViewModelFactory(private val surahNumber: Int) :
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SurahDetailViewModel::class.java)) {
                return SurahDetailViewModel(surahNumber) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }  }