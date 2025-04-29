// File: app/src/main/java/com/example/quranapp/ui/navigation/AppNavigation.kt
package com.example.quranapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quranapp.ui.screens.AppSplashScreen
import com.example.quranapp.ui.screens.HomeScreen
import com.example.quranapp.AuthScreen
import com.example.quranapp.ui.screens.SlidingScreen
import com.example.quranapp.ui.screens.JuzScreen
import com.example.quranapp.ui.screens.JuzSurahListScreen
import com.example.quranapp.ui.screens.SurahListScreen
import com.example.quranapp.ui.screens.ReadingHistoryScreen
import com.example.quranapp.ui.screens.FavoriteVersesScreen
import com.example.quranapp.ui.screens.SurahDetailScreen
import com.example.quranapp.ui.screens.UserInfoScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            AppSplashScreen(navController)
        }
        composable("auth") {
            AuthScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("user_info") {
            UserInfoScreen(navController)
        }
        composable("sliding") {
            SlidingScreen(navController)
        }
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
        composable("list") {
            SurahListScreen(navController)
        }
        composable("riwayat_baca") {
            ReadingHistoryScreen(navController)
        }
        composable("favorit_ayat") {
            FavoriteVersesScreen(navController)
        }
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