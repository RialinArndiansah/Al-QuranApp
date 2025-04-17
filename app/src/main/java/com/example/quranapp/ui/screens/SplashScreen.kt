package com.example.quranapp.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.quranapp.R
import kotlinx.coroutines.delay

@Composable
fun AppSplashScreen(navController: NavHostController) {
    // Animasi untuk logo
    val logoAlphaAnim = remember { Animatable(0f) }
    val logoScaleAnim = remember { Animatable(0.5f) }
    val logoTranslationYAnim = remember { Animatable(-300f) }

    // Animasi untuk latar belakang
    val bgAlphaAnim = remember { Animatable(0f) }
    val bgScaleAnim = remember { Animatable(1.2f) }

    // Jalankan animasi saat layar splash dimulai
    LaunchedEffect(true) {
        // Animasi latar belakang
        bgAlphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )
        bgScaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )

        // Animasi logo
        logoAlphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        logoScaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        logoTranslationYAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000)
        )

        // Tunggu 1 detik setelah animasi selesai
        delay(1000)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00796B)) // Warna cadangan jika gambar gagal dimuat
    ) {
        // Latar belakang dengan animasi
        Image(
            painter = painterResource(id = R.drawable.bg2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(bgAlphaAnim.value)
                .scale(bgScaleAnim.value),
            contentScale = ContentScale.Crop
        )

        // Logo dengan animasi
        Image(
            painter = painterResource(id = R.drawable.logoaplikasi),
            contentDescription = "App Logo",
            modifier = Modifier
                .alpha(logoAlphaAnim.value)
                .scale(logoScaleAnim.value)
                .graphicsLayer(translationY = logoTranslationYAnim.value)
        )
    }
}