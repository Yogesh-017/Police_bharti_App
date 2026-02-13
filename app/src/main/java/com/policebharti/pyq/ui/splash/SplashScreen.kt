package com.policebharti.pyq.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.policebharti.pyq.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Splash Screen
 * ─ App logo + motivational quote (Marathi + English).
 * ─ Auto-navigates to Login after 2.5 seconds.
 * ─ Premium gradient background with subtle fade-in animation.
 */
@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {

    // Auto-navigate after delay
    LaunchedEffect(Unit) {
        delay(2500L)
        onNavigateToLogin()
    }

    // Fade-in animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1200, easing = EaseOutCubic),
        label = "splash_fade"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd, DeepNavy)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .alpha(alpha)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── App Logo / Title ──
            Text(
                text = "🛡️",
                fontSize = 72.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Police Bharti",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGold,
                style = MaterialTheme.typography.displayLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            Text(
                text = "PYQ Practice",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.85f),
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ── Motivational Quote ──
            Text(
                text = "\"जिद्द असेल तर मार्ग सापडतोच.\"",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = AccentGold.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "If you have determination, you will find the way.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // ── Loading indicator ──
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = AccentGold,
                strokeWidth = 2.dp
            )
        }

        // ── Disclaimer at bottom ──
        Text(
            text = "⚠ Screen recording & screenshots are disabled for security.",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.35f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}
