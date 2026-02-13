package com.policebharti.pyq.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.policebharti.pyq.data.db.AppDatabase

/**
 * WorkManager worker that runs every 12 hours to clean up
 * expired content packs (older than 3 days).
 *
 * 1. Marks packs where expires_at < now as expired.
 * 2. Deletes expired pack records (cascades to questions).
 */
class PackExpiryWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val db = AppDatabase.getInstance(applicationContext)
            val dao = db.contentPackDao()

            // Step 1: Mark expired packs
            dao.markExpiredPacks()

            // Step 2: Delete expired records (CASCADE removes related questions)
            dao.deleteExpiredPacks()

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
