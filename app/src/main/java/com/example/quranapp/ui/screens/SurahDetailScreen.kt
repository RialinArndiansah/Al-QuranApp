package com.example.quranapp.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quranapp.R
import com.example.quranapp.data.manager.AudioManager
import com.example.quranapp.data.repository.FavoritesRepository
import com.example.quranapp.util.AudioPlayerWrapper
import com.example.quranapp.viewmodel.ReadingHistoryInsertViewModel
import com.example.quranapp.viewmodel.SurahDetailViewModel
import com.example.quranapp.viewmodel.SurahDetailViewModel.SurahDetailViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun SurahDetailScreen(
    surahNumber: Int,
    navController: NavController,
    targetAyah: Int = -1
) {
    val viewModel: SurahDetailViewModel =
        viewModel(factory = SurahDetailViewModelFactory(surahNumber))
    val detail by viewModel.detail
    val translation by viewModel.translation
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage
    val context = LocalContext.current
    val headerFont = FontFamily(Font(R.font.headerfont))
    val indonesiaFont = FontFamily(Font(R.font.indonesia))
    val arabicFont = FontFamily(Font(R.font.amiri))

    val historyInsertViewModel: ReadingHistoryInsertViewModel = viewModel()
    val listState = rememberLazyListState()
    var historySaved by remember { mutableStateOf(false) }
    var isSurahPlaying by remember { mutableStateOf(false) }
    var isSurahPaused by remember { mutableStateOf(false) }
    var ayahAudioRefresh by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Simpan riwayat saat ayat pertama dimuat
    LaunchedEffect(detail) {
        if (!historySaved && detail != null && detail?.ayahs?.isNotEmpty() == true) {
            try {
                historyInsertViewModel.addHistory(
                    detail!!.number,
                    detail!!.ayahs.first().number,
                    detail!!.ayahs.first().text
                )
                historySaved = true
                Log.d("SurahDetailScreen", "Initial history saved for surah ${detail!!.number}")
            } catch (e: Exception) {
                Log.e("SurahDetailScreen", "Error saving initial history: ${e.message}", e)
            }
        }
        isSurahPlaying = false
        isSurahPaused = false
        try {
            AudioPlayerWrapper.stop()
        } catch (e: Exception) {
            Log.e("SurahDetailScreen", "Error stopping audio: ${e.message}", e)
        }
        ayahAudioRefresh++
    }

    // Perbarui riwayat berdasarkan posisi gulir
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val firstVisibleAyah = listState.firstVisibleItemIndex + 1
        if (detail != null && detail?.ayahs?.isNotEmpty() == true && firstVisibleAyah <= detail!!.ayahs.size) {
            try {
                val lastAyah = detail!!.ayahs[minOf(firstVisibleAyah - 1, detail!!.ayahs.size - 1)].number
                historyInsertViewModel.addHistory(
                    detail!!.number,
                    lastAyah,
                    detail!!.ayahs.first { it.number == lastAyah }.text
                )
                Log.d("SurahDetailScreen", "Updated history for ayah $lastAyah in surah ${detail!!.number}")
            } catch (e: Exception) {
                Log.e("SurahDetailScreen", "Error updating history: ${e.message}", e)
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    } else if (error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error: $error", modifier = Modifier.padding(16.dp))
        }
    } else if (detail != null && translation != null && detail?.ayahs?.isNotEmpty() == true) {
        val audioUrls = detail!!.ayahs.mapNotNull { ayah ->
            try {
                AudioManager.getAudioUrl(ayah.number)
            } catch (e: Exception) {
                Log.e("SurahDetailScreen", "Error getting audio URL for ayah ${ayah.number}: ${e.message}", e)
                null
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.headerbaground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            try {
                                navController.popBackStack()
                            } catch (e: Exception) {
                                Log.e("SurahDetailScreen", "Error navigating back: ${e.message}", e)
                            }
                        },
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            try {
                                if (audioUrls.isNotEmpty()) {
                                    if (!isSurahPlaying) {
                                        isSurahPlaying = true
                                        isSurahPaused = false
                                        AudioPlayerWrapper.playList(context, audioUrls, 0) {
                                            isSurahPlaying = false
                                            isSurahPaused = false
                                            ayahAudioRefresh++
                                        }
                                    } else {
                                        AudioPlayerWrapper.togglePlayPause()
                                        isSurahPaused = !isSurahPaused
                                        ayahAudioRefresh++
                                    }
                                } else {
                                    Toast.makeText(context, "No audio available for this surah", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Log.e("SurahDetailScreen", "Error playing surah audio: ${e.message}", e)
                                Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                    ) {
                        Icon(
                            imageVector = if (isSurahPlaying && !isSurahPaused) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isSurahPlaying && !isSurahPaused) "Pause Surah" else "Play Surah",
                            tint = Color.White
                        )
                    }
                }
                Text(
                    text = detail?.englishName ?: "Unknown Surah",
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = arabicFont),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFF5F5F5)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(detail!!.ayahs) { index, ayah ->
                    val highlight = targetAyah == ayah.number
                    var showOptions by remember { mutableStateOf(false) }
                    var isFavorite by remember { mutableStateOf(FavoritesRepository.isFavorite(detail!!.number, ayah.number)) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showOptions = !showOptions }
                            .padding(vertical = 8.dp)
                            .background(if (highlight) Color.Yellow.copy(alpha = 0.3f) else Color.Transparent)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.bgnumber),
                                contentDescription = "Background Number",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelMedium.copy(fontFamily = headerFont),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = ayah.text,
                            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = arabicFont),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                        ayah.textTransliteration?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 2.dp)
                            )
                        }
                        val ayahTranslation = translation?.ayahs?.getOrNull(index)
                        ayahTranslation?.let {
                            Text(
                                text = it.text,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = indonesiaFont),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                        if (showOptions) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    try {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Ayat", ayah.text)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Ayat copied", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        Log.e("SurahDetailScreen", "Error copying ayah: ${e.message}", e)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = "Copy Ayah"
                                    )
                                }
                                IconButton(onClick = {
                                    try {
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, ayah.text)
                                            type = "text/plain"
                                        }
                                        val shareIntent = Intent.createChooser(sendIntent, null)
                                        context.startActivity(shareIntent)
                                    } catch (e: Exception) {
                                        Log.e("SurahDetailScreen", "Error sharing ayah: ${e.message}", e)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Share,
                                        contentDescription = "Share Ayah"
                                    )
                                }
                                IconButton(onClick = {
                                    try {
                                        if (isFavorite) {
                                            FavoritesRepository.removeFavorite(detail!!.number, ayah.number)
                                            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                                        } else {
                                            FavoritesRepository.addFavorite(detail!!.number, ayah.number, ayah.text)
                                            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                                        }
                                        isFavorite = !isFavorite
                                    } catch (e: Exception) {
                                        Log.e("SurahDetailScreen", "Error toggling favorite: ${e.message}", e)
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites"
                                    )
                                }
                                IconButton(onClick = {
                                    Toast.makeText(context, "History already recorded", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.History,
                                        contentDescription = "History"
                                    )
                                }
                                val audioUrl = try {
                                    AudioManager.getAudioUrl(ayah.number)
                                } catch (e: Exception) {
                                    Log.e("SurahDetailScreen", "Error getting audio URL for ayah ${ayah.number}: ${e.message}", e)
                                    null
                                }
                                val isCurrentlyPlaying = audioUrl != null && AudioPlayerWrapper.getCurrentUrl() == audioUrl && AudioPlayerWrapper.isPlaying()
                                IconButton(onClick = {
                                    try {
                                        if (audioUrl != null) {
                                            if (AudioPlayerWrapper.getCurrentUrl() == audioUrl) {
                                                AudioPlayerWrapper.togglePlayPause()
                                                ayahAudioRefresh++
                                            } else {
                                                AudioPlayerWrapper.play(context, audioUrl) {
                                                    ayahAudioRefresh++
                                                    scope.launch {
                                                        try {
                                                            historyInsertViewModel.addHistory(detail!!.number, ayah.number, ayah.text)
                                                        } catch (e: Exception) {
                                                            Log.e("SurahDetailScreen", "Error saving history after audio: ${e.message}", e)
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(context, "Audio not available for this ayah", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Log.e("SurahDetailScreen", "Error playing ayah audio: ${e.message}", e)
                                        Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isCurrentlyPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                        contentDescription = if (isCurrentlyPlaying) "Pause Audio" else "Play Audio"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No data available", modifier = Modifier.padding(16.dp))
        }
    }
}