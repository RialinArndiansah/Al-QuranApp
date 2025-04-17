package com.example.quranapp.data.repository

import androidx.compose.runtime.mutableStateListOf

data class AyahFavorite(val surahNumber: Int, val ayahNumber: Int, val text: String)

object FavoritesRepository {
    private val favorites = mutableStateListOf<AyahFavorite>()

    fun addFavorite(surahNumber: Int, ayahNumber: Int, text: String) {
        if (!isFavorite(surahNumber, ayahNumber)) {
            favorites.add(AyahFavorite(surahNumber, ayahNumber, text))
        }
    }

    fun removeFavorite(surahNumber: Int, ayahNumber: Int) {
        favorites.removeAll { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
    }

    fun isFavorite(surahNumber: Int, ayahNumber: Int): Boolean {
        return favorites.any { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
    }

    fun getFavorites(): List<AyahFavorite> {
        return favorites.toList()
    }
}