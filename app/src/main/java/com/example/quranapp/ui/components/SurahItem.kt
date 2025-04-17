package com.example.quranapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quranapp.R
import com.example.quranapp.data.model.Surah
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

// Instance custom font menggunakan file indonesia.ttf
private val indonesiaFont = FontFamily(Font(R.font.indonesia))

@Composable
fun SurahItem(
    surah: Surah,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Baris nomor surah dan nama (bahasa Arab)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nomor surah dengan background gambar bgnumber.png
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bgnumber),
                        contentDescription = "Background Nomor Surah",
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
                // Nama surah (bahasa Arab) dengan perataan kanan
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = surah.name,
                        style = MaterialTheme.typography.headlineSmall.copy(fontFamily = indonesiaFont),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            // Nama surah dalam bahasa Inggris (rincian surah diberi warna hijau dan custom font)
            Text(
                text = surah.englishName,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = indonesiaFont),
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
            // Detail tambahan: tipe wahyu dan jumlah ayat (rincian surah diberi warna hijau dan custom font)
            Text(
                text = "${surah.revelationType} - ${surah.numberOfAyahs} Ayat",
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = indonesiaFont),
                color = Color.Cyan,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
