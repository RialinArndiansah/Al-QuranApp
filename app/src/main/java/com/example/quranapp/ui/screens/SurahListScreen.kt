package com.example.quranapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.example.quranapp.data.model.allSurah

@Composable
fun SurahListScreen(navController: NavHostController) {
    val indonesiaFont = FontFamily(Font(R.font.indonesia))
    val arabicFont = FontFamily(Font(R.font.amiri))
    val surahList = allSurah

    var query by remember { mutableStateOf("") }
    var showSearchField by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var filterType by remember { mutableStateOf("Nomor") }
    val filterOptions = listOf("Nomor", "Nama")

    val filteredSurahList = surahList.filter {
        when (filterType) {
            "Nomor" -> it.number.toString().contains(query)
            "Nama" -> it.name.contains(query, ignoreCase = true) || it.arabicName.contains(query, ignoreCase = true)
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Soft gray background
    ) {
        // Header with background image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
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
                        text = "DAFTAR SURAH",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = indonesiaFont)
                    )
                }
                IconButton(onClick = { showSearchField = !showSearchField }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Surah",
                        tint = Color.White
                    )
                }
            }
        }

        // Search field and filter dropdown
        if (showSearchField) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Cari Surah", style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text(filterType, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont))
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        filterOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont)) },
                                onClick = {
                                    filterType = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Surah list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            items(filteredSurahList) { surah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable { navController.navigate("detail/${surah.number}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header: number and Arabic name
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.bgnumber),
                                    contentDescription = "Background Number",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Text(
                                    text = surah.number.toString(),
                                    style = MaterialTheme.typography.labelMedium.copy(fontFamily = indonesiaFont),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = surah.arabicName,
                                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = arabicFont),
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        // Display transliterated name
                        Text(
                            text = surah.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        // English name
                        Text(
                            text = surah.englishName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        // Revelation type and ayah count
                        Text(
                            text = "${surah.revelationType} - ${surah.numberOfAyahs} ayat",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = indonesiaFont),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}