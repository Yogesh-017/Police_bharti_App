package com.policebharti.pyq.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.policebharti.pyq.ui.theme.*

/**
 * AI Help Modal — LOCKED until all questions solved.
 *
 * Shows a dialog explaining:
 * - AI assistance is locked.
 * - Support via ₹5 vote to unlock in the future.
 *
 * This is a placeholder for a future paid AI feature.
 */
@Composable
fun AiHelpModal(
    isVisible: Boolean,
    allQuestionsSolved: Boolean = false,
    onDismiss: () -> Unit,
    onVote: () -> Unit
) {
    if (!isVisible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = if (allQuestionsSolved) AccentGold else NotVisitedGrey,
                modifier = Modifier.size(40.dp)
            )
        },
        title = {
            Text(
                text = if (allQuestionsSolved) "AI Help Available!"
                       else "🔒 AI Help Locked",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!allQuestionsSolved) {
                    Text(
                        text = "Complete all questions to unlock AI explanations.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = "Made by a single developer 🧑‍💻",
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "AI costs money to run. You can support this app via a ₹5 vote to help bring AI features!",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )

                    // App is made with love
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AccentGold.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "❤ हे ॲप एका डेव्हलपरने बनवले आहे.\nAI खर्चिक आहे, ₹5 मताद्वारे सपोर्ट करा.",
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = AccentAmber,
                            lineHeight = 18.sp
                        )
                    }
                } else {
                    Text(
                        text = "🎉 You solved all questions! AI explanations will be available in a future update.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onVote,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGold)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Support ₹5 Vote",
                    fontWeight = FontWeight.SemiBold,
                    color = DeepNavy
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Maybe Later")
            }
        }
    )
}
