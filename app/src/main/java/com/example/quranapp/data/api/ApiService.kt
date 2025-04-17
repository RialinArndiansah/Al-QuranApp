package com.example.quranapp.data.api

import com.example.quranapp.data.model.AudioEditionResponse
import com.example.quranapp.data.model.SurahResponse
import com.example.quranapp.data.model.SurahDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("surah")
    suspend fun getSurahList(): SurahResponse

    @GET("surah/{number}/quran-uthmani")
    suspend fun getSurahDetail(
        @Path("number") number: Int
    ): SurahDetailResponse

    @GET("surah/{number}/id.indonesian")
    suspend fun getSurahTranslation(
        @Path("number") number: Int
    ): SurahDetailResponse


    @GET("edition?format=audio&language=fr&type=versebyverse")
    suspend fun getAudioEditions(): AudioEditionResponse
}
