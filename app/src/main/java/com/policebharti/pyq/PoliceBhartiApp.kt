package com.policebharti.pyq

import android.app.Application
import androidx.work.*
import com.policebharti.pyq.worker.PackExpiryWorker
import java.util.concurrent.TimeUnit

/**
 * Application class — entry point for global initialization.
 * Schedules the periodic pack-expiry worker on app start.
 */
class PoliceBhartiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        schedulePackExpiryWorker()
    }

    /**
     * Enqueue a periodic WorkManager job that runs every 12 hours
     * to delete content packs older than 3 days.
     */
    private fun schedulePackExpiryWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val expiryRequest = PeriodicWorkRequestBuilder<PackExpiryWorker>(
            12, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "pack_expiry_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            expiryRequest
        )
    }
}
