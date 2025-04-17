package com.example.quranapp.data.manager

object AudioManager {
    private var audioIdentifier = "ar.alafasy" // Now variable for flexibility
    private const val baseUrl = "https://cdn.islamic.network/quran/audio/128"

    // New function to update the audio identifier (e.g., based on user settings)
    fun setAudioEdition(identifier: String) {
       audioIdentifier = identifier
    }

    // Fungsi untuk mengontruksi URL audio per ayah
    // Contoh output untuk ayat 1: https://cdn.islamic.network/quran/audio/128/ar.alafasy/1.mp3
    fun getAudioUrl(ayah: Int): String {
        return "$baseUrl/$audioIdentifier/$ayah.mp3"
    }
}
