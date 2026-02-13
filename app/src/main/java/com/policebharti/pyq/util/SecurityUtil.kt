package com.policebharti.pyq.util

import android.view.WindowManager
import androidx.activity.ComponentActivity

/**
 * Security utilities — prevents screenshots and screen recording.
 */
object SecurityUtil {

    /**
     * Apply FLAG_SECURE to the activity window.
     * Call in onCreate() of every Activity.
     */
    fun applyScreenshotProtection(activity: ComponentActivity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}
