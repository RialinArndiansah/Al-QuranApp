// File: app/src/main/java/com/example/quranapp/data/repository/QuranRepository.kt
package com.example.quranapp.data.repository

import com.example.quranapp.data.api.ApiService
import com.example.quranapp.data.api.RetrofitClient

class QuranRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {
    suspend fun getSurahList() = apiService.getSurahList()

    suspend fun getSurahDetail(number: Int) = apiService.getSurahDetail(number)

    suspend fun getSurahTranslation(number: Int) = apiService.getSurahTranslation(number)

    suspend fun getAudioEditions() = apiService.getAudioEditions()

}