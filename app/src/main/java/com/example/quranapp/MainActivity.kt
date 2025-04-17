package com.example.quranapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quranapp.ui.navigation.AppNavigation
import com.example.quranapp.ui.theme.AlQuranTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlQuranTheme {
                AppNavigation()
            }
        }
    }
}
