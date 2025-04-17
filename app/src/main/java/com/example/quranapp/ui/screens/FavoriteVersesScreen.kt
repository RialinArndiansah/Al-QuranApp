package com.example.quranapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quranapp.R

// Data model untuk ayat favorit (contoh sederhana)
data class FavoriteVerse(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val translation: String
)

// Daftar ayat favorit (bisa diganti dengan data dari penyimpanan lokal)
val favoriteVerses = listOf(
    FavoriteVerse(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Dengan nama Allah, Yang Maha Pengasih, Yang Maha Penyayang"),
    FavoriteVerse(2, 255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "Allah, tidak ada tuhan selain Dia, Yang Maha Hidup, Yang terus-menerus mengurus"),
    FavoriteVerse(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Katakanlah: Dia adalah Allah, Yang Maha Esa")
)

@Composable
fun FavoriteVersesScreen(navController: NavHostController) {
    val indonesiaFont = FontFamily(Font(R.font.indonesia))
    val arabicFont = FontFamily(Font(R.font.amiri))
    val headerFont = FontFamily(Font(R.font.headerfont))

    var query by remember { mutableStateOf("") }
    var showSearchField by remember { mutableStateOf(false) }

    val filteredVerses = favoriteVerses.filter {
        it.arabicText.contains(query, ignoreCase = true) || it.translation.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header with background image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.headerbaground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "AYAT FAVORIT",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = headerFont)
                    )
                }
                IconButton(onClick = { showSearchField = !showSearchField }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Verses",
                        tint = Color.White
                    )
                }
            }
        }

        // Search field
        if (showSearchField) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Cari Ayat", style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont)) }
            )
        }

        // Verses list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            items(filteredVerses) { verse ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable { /* Handle click to view verse detail if needed */ },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Arabic text
                        Text(
                            text = verse.arabicText,
                            style = MaterialTheme.typography.headlineSmall.copy(fontFamily = arabicFont),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Translation
                        Text(
                            text = "${verse.surahNumber}:${verse.ayahNumber} - ${verse.translation}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}