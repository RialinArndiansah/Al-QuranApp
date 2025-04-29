package com.example.quranapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quranapp.AuthScreen
import com.example.quranapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var name by remember { mutableStateOf(sharedPrefs.getString("name", null)) }
    var email by remember { mutableStateOf(sharedPrefs.getString("email", null)) }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.bg2),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (name == null || email == null) {
                AuthScreen(navController)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.logomesjid1),
                        contentDescription = "Mosque Logo",
                        modifier = Modifier
                            .size(350.dp)
                            .padding(bottom = 32.dp)
                    )

                    MenuCard(Icons.Default.Person, "Profil") {
                        navController.navigate("user_info")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    MenuCard(Icons.Default.Book, "Baca Al-Quran") {
                        navController.navigate("sliding")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    MenuCard(Icons.Default.History, "Riwayat Baca") {
                        navController.navigate("riwayat_baca")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    MenuCard(Icons.Default.Favorite, "Ayat Favorit") {
                        navController.navigate("favorit_ayat")
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCard(icon: ImageVector, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00796B),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color(0xFF00796B),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}