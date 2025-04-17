package com.example.quranapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quranapp.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        // Home screen
        composable("home") {
            HomeScreen(navController)
        }

        // Sliding screen (menampilkan swipe antar surah)
        composable("sliding") {
            SlidingScreen(navController)
        }

        // Juz screens
        composable("juz") {
            JuzScreen(navController)
        }
        composable(
            route = "juz_surah_list/{juzNumber}",
            arguments = listOf(
                navArgument("juzNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val juzNumber = backStackEntry.arguments?.getInt("juzNumber") ?: 1
            JuzSurahListScreen(navController, juzNumber)
        }

        // Surah list screen
        composable("list") {
            SurahListScreen(navController)
        }

        // Reading history screen
        composable("riwayat_baca") {
            ReadingHistoryScreen(navController)
        }

        // Favorite verses screen
        composable("favorit_ayat") {
            FavoriteVersesScreen(navController)
        }

        // Surah detail screen (untuk menampilkan 1 surah saja)
        composable(
            route = "detail/{surahNumber}?targetAyah={targetAyah}",
            arguments = listOf(
                navArgument("surahNumber") { type = NavType.IntType },
                navArgument("targetAyah") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
            val targetAyah = backStackEntry.arguments?.getInt("targetAyah") ?: -1
            SurahDetailScreen(surahNumber, navController, targetAyah)
        }
    }
}