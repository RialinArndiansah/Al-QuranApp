package com.example.quranapp.data.model

data class SurahDetailResponse(
    val data: SurahDetail
)

data class SurahDetail(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val ayahs: List<Ayah>
)

data class Ayah(
    val number: Int,
    val text: String,
    val textTransliteration: String? = null
)
