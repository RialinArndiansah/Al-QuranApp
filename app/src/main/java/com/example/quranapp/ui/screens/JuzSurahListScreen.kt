package com.example.quranapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.quranapp.data.local.juzList
import com.example.quranapp.data.model.allSurah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzSurahListScreen(navController: NavHostController, juzNumber: Int) {
    val juz = juzList.find { it.number == juzNumber }
    val surahList = allSurah.filter { surah ->
        juz != null && surah.number in (juz.startSurah..juz.endSurah)
    }

    val indonesiaFont = FontFamily(Font(R.font.indonesia))
    val headerFont = FontFamily(Font(R.font.headerfont))

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "DAFTAR SURAH $juzNumber",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = headerFont)
                    )
                    Box(modifier = Modifier.size(48.dp))
                }
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            items(surahList) { surah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable {
                            navController.navigate("detail/${surah.number}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(40.dp)
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
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = surah.name,
                                    style = MaterialTheme.typography.headlineSmall.copy(fontFamily = indonesiaFont),
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Text(
                            text = surah.englishName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont),
                            color = Color.Cyan,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "${surah.revelationType} - ${surah.numberOfAyahs} ayat",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = indonesiaFont),
                            color = Color.Cyan,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}