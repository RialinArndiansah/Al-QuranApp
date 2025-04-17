package com.example.quranapp.util

import android.content.Context
import android.media.MediaPlayer

object AudioPlayerWrapper {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlayingInternal: Boolean = false
    private var currentUrlInternal: String? = null
    private var playList: List<String>? = null
    private var playListIndex: Int = 0
    private var isPaused: Boolean = false

    fun play(context: Context, url: String, onCompletion: () -> Unit = {}) {
        stop()
        isPaused = false
        currentUrlInternal = url
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener {
                start()
                isPlayingInternal = true
            }
            setOnCompletionListener {
                release()
                mediaPlayer = null
                isPlayingInternal = false
                currentUrlInternal = null
                onCompletion()
            }
            setOnErrorListener { mp, _, _ ->
                mp.release()
                mediaPlayer = null
                isPlayingInternal = false
                currentUrlInternal = null
                true
            }
            prepareAsync()
        }
    }

    fun playList(context: Context, urls: List<String>, startIndex: Int = 0, onEnd: () -> Unit = {}) {
        if (urls.isEmpty()) return
        playList = urls
        playListIndex = startIndex
        isPaused = false
        playNextInList(context, onEnd)
    }

    private fun playNextInList(context: Context, onEnd: () -> Unit) {
        val urls = playList ?: return
        if (playListIndex >= urls.size) {
            stop()
            onEnd()
            return
        }
        currentUrlInternal = urls[playListIndex]
        mediaPlayer = MediaPlayer().apply {
            setDataSource(urls[playListIndex])
            setOnPreparedListener {
                start()
                isPlayingInternal = true
            }
            setOnCompletionListener {
                release()
                mediaPlayer = null
                isPlayingInternal = false
                currentUrlInternal = null
                if (!isPaused) {
                    playListIndex += 1
                    playNextInList(context, onEnd)
                }
            }
            setOnErrorListener { mp, _, _ ->
                mp.release()
                mediaPlayer = null
                isPlayingInternal = false
                currentUrlInternal = null
                true
            }
            prepareAsync()
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlayingInternal = false
                isPaused = true
            } else {
                it.start()
                isPlayingInternal = true
                isPaused = false
            }
        }
    }

    fun stop() {
        mediaPlayer?.apply {
            if (isPlayingInternal) stop()
            release()
        }
        mediaPlayer = null
        isPlayingInternal = false
        currentUrlInternal = null
        playList = null
        playListIndex = 0
        isPaused = false
    }

    fun isPlaying(): Boolean = isPlayingInternal

    fun isPaused(): Boolean = isPaused

    fun getCurrentUrl(): String? = currentUrlInternal
}