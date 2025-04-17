package com.example.quranapp.worker


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quranapp.data.repository.QuranRepository

class SurahFetchWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository = QuranRepository()

    override suspend fun doWork(): Result {
        return try {
            // Contoh: Ambil daftar surah dan simpan ke cache atau Room Database jika ingin diimplementasikan
            repository.getSurahList()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
