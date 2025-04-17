package com.example.quranapp.data.model

data class AudioEditionResponse(
    val data: List<AudioEdition>
)

data class AudioEdition(
    val identifier: String,
    val englishName: String,
    val name: String,
    val format: String,
    val type: String,
    val language: String
)
