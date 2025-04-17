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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quranapp.R
import com.example.quranapp.viewmodel.ReadingHistoryViewModel

@Composable
fun ReadingHistoryScreen(navController: NavHostController) {
    val indonesiaFont = FontFamily(Font(R.font.indonesia))
    val arabicFont = FontFamily(Font(R.font.amiri))
    val headerFont = FontFamily(Font(R.font.headerfont))
    val viewModel: ReadingHistoryViewModel = viewModel()

    var query by remember { mutableStateOf("") }
    var showSearchField by remember { mutableStateOf(false) }

    // Amati perubahan history dari ViewModel
    val history by viewModel.historyItems.collectAsState(initial = emptyList())

    val filteredHistory = history.filter {
        it.surah.name.contains(query, ignoreCase = true) ||
                it.surah.arabicName.contains(query, ignoreCase = true) ||
                it.lastReadTime.contains(query, ignoreCase = true)
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
                        text = "RIWAYAT MEMBACA",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = headerFont)
                    )
                }
                IconButton(onClick = { showSearchField = !showSearchField }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search History",
                        tint = Color.White
                    )
                }
            }
        }

        // Search field
        if (showSearchField) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; viewModel.searchHistory(it) }, // Panggil search saat query berubah
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Cari Riwayat", style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont)) }
            )
        }

        // History list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            items(filteredHistory) { history ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable {
                            navController.navigate("detail/${history.surah.number}?targetAyah=${history.lastAyah}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Surah Arabic name and number
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = history.surah.number.toString(),
                                style = MaterialTheme.typography.labelMedium.copy(fontFamily = indonesiaFont),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = history.surah.arabicName,
                                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = arabicFont),
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        // Transliterated name and English name
                        Text(
                            text = history.surah.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = history.surah.englishName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        // Last read details
                        Text(
                            text = "Terakhir dibaca: Ayat ${history.lastAyah}, ${history.lastReadTime}",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = indonesiaFont),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}