package com.example.quranapp.util

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

fun playAudio(context: Context, url: String) {
    val mediaPlayer = MediaPlayer()
    try {
        mediaPlayer.setDataSource(url)
        mediaPlayer.setOnPreparedListener { mp ->
            mp.start()
        }
        mediaPlayer.setOnCompletionListener { mp ->
            mp.release()
        }
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e("MediaPlayer", "Error what: $what extra: $extra")
            mp.release()
            true
        }
        mediaPlayer.prepareAsync()
    } catch (e: Exception) {
        e.printStackTrace()
        mediaPlayer.release()
    }
}
