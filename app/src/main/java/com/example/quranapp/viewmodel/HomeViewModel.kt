package com.example.quranapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    // Example state holding a welcome message
    private val _welcomeMessage = MutableStateFlow("Assalamu'alaikum, Selamat Datang!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage
}