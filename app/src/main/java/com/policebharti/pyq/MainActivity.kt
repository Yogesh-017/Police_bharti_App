package com.policebharti.pyq

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.policebharti.pyq.ui.Navigation
import com.policebharti.pyq.ui.theme.PoliceBhartiTheme

/**
 * Single-Activity entry point.
 * - Applies FLAG_SECURE to prevent screenshots and screen recording.
 * - Sets Compose content with the app theme and navigation graph.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── Security: prevent screenshots & screen recording ──
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContent {
            PoliceBhartiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }
}
