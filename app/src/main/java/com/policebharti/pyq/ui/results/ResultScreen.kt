package com.policebharti.pyq.ui.results

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.policebharti.pyq.ui.theme.*

/**
 * Result Screen — PREMIUM scorecard shown after test completion.
 *
 * Features:
 * ─ Animated circular score gauge with percentage.
 * ─ Large "marks obtained / total" display.
 * ─ Color-coded stat cards (correct, wrong, unanswered).
 * ─ Motivational message based on score.
 * ─ Buttons: Review Answers, Share, Retake, Go Home.
 *
 * Designed to WOW the user and encourage them to keep practicing.
 */
@Composable
fun ResultScreen(
    correctCount: Int = 72,
    wrongCount: Int = 18,
    unansweredCount: Int = 10,
    totalQuestions: Int = 100,
    scorePercentage: Float = 72f,
    district: String = "Pune",
    year: String = "2023",
    setName: String = "Set A",
    onReviewAnswers: () -> Unit = {},
    onRetake: () -> Unit = {},
    onGoHome: () -> Unit = {},
    onViewVote: () -> Unit = {}
) {
    // ── Animate the score on entry ──
    val animatedScore by animateFloatAsState(
        targetValue = scorePercentage,
        animationSpec = tween(durationMillis = 1500, easing = EaseOutCubic),
        label = "score_anim"
    )
    val scaleAnim by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale_anim"
    )

    // Motivational message based on score
    val (emoji, message, subMessage) = when {
        scorePercentage >= 90 -> Triple("🏆", "Outstanding!", "तुम्ही अभ्यासात अव्वल आहात! Keep it up!")
        scorePercentage >= 75 -> Triple("🌟", "Great Job!", "छान प्रगती! You're on the right track!")
        scorePercentage >= 60 -> Triple("💪", "Good Effort!", "अजून थोडं करा, यश जवळ आहे!")
        scorePercentage >= 40 -> Triple("📖", "Keep Practicing!", "सराव हेच यशाचं सूत्र आहे. Don't give up!")
        else -> Triple("🔥", "Don't Stop!", "हार मानू नका! Every attempt makes you stronger!")
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Header with gradient ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GradientStart, GradientEnd)
                        )
                    )
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // ── Animated Score Circle ──
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .scale(scaleAnim),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background circle
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = Color.White.copy(alpha = 0.15f),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        // Score arc
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(AccentGold, CorrectGreen, AccentGold)
                                ),
                                startAngle = -90f,
                                sweepAngle = (animatedScore / 100f) * 360f,
                                useCenter = false,
                                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        // Score text inside circle
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = emoji,
                                fontSize = 36.sp
                            )
                            Text(
                                text = "${animatedScore.toInt()}%",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AccentGold,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    shadow = Shadow(Color.Black.copy(0.3f), Offset(1f, 1f), 4f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Marks obtained ──
                    Text(
                        text = "$correctCount / $totalQuestions",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Marks Obtained",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // ── Paper info ──
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "$district • $year • $setName",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Motivational message ──
                    Text(
                        text = message,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold
                    )
                    Text(
                        text = subMessage,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Stat Cards Row ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CheckCircle,
                    iconColor = CorrectGreen,
                    label = "Correct",
                    value = "$correctCount",
                    bgColor = CorrectGreen.copy(alpha = 0.1f)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Cancel,
                    iconColor = WrongRed,
                    label = "Wrong",
                    value = "$wrongCount",
                    bgColor = WrongRed.copy(alpha = 0.1f)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.RemoveCircle,
                    iconColor = NotVisitedGrey,
                    label = "Skipped",
                    value = "$unansweredCount",
                    bgColor = NotVisitedGrey.copy(alpha = 0.15f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Accuracy & Speed Row ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val accuracy = if ((correctCount + wrongCount) > 0)
                    (correctCount.toFloat() / (correctCount + wrongCount) * 100).toInt()
                else 0
                val attempted = correctCount + wrongCount

                InfoCard(
                    modifier = Modifier.weight(1f),
                    label = "Accuracy",
                    value = "$accuracy%",
                    icon = Icons.Default.TrackChanges,
                    color = RoyalBlue
                )
                InfoCard(
                    modifier = Modifier.weight(1f),
                    label = "Attempted",
                    value = "$attempted / $totalQuestions",
                    icon = Icons.Default.EditNote,
                    color = AccentAmber
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Action Buttons ──
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Review Answers
                Button(
                    onClick = onReviewAnswers,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Review Answers", fontWeight = FontWeight.SemiBold)
                }

                // Retake Test
                OutlinedButton(
                    onClick = onRetake,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = RoyalBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Retake Test", color = RoyalBlue, fontWeight = FontWeight.SemiBold)
                }

                // Support / Vote
                OutlinedButton(
                    onClick = onViewVote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, AccentGold)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = AccentGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Support This App ❤️", color = AccentAmber, fontWeight = FontWeight.SemiBold)
                }

                // Go Home
                TextButton(
                    onClick = onGoHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Back to Home", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    bgColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = iconColor
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = iconColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}
