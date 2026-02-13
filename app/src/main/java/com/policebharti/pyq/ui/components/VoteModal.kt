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
 * Vote Modal — Yes/No vote + cloud count display.
 *
 * Vote data is stored locally and synced to cloud when online.
 * Shows community vote counts fetched from the server.
 */
@Composable
fun VoteModal(
    isVisible: Boolean,
    yesCount: Int = 342,
    noCount: Int = 18,
    hasVoted: Boolean = false,
    onVoteYes: () -> Unit = {},
    onVoteNo: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (!isVisible) return

    val totalVotes = yesCount + noCount

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                Icons.Default.HowToVote,
                contentDescription = null,
                tint = AccentGold,
                modifier = Modifier.size(40.dp)
            )
        },
        title = {
            Text(
                text = "Support This App",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Would you support adding AI features with a ₹5 donation?",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )

                // Vote counts display
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Community Votes",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("👍", fontSize = 24.sp)
                                Text(
                                    "$yesCount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = CorrectGreen
                                )
                                Text("Yes", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("👎", fontSize = 24.sp)
                                Text(
                                    "$noCount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = WrongRed
                                )
                                Text("No", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                        }
                        if (totalVotes > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { yesCount.toFloat() / totalVotes },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                                color = CorrectGreen,
                                trackColor = WrongRed.copy(alpha = 0.3f)
                            )
                        }
                    }
                }

                if (hasVoted) {
                    Text(
                        "✅ Thank you for voting!",
                        fontWeight = FontWeight.SemiBold,
                        color = CorrectGreen,
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            if (!hasVoted) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onVoteYes,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CorrectGreen)
                    ) {
                        Text("👍 Yes!", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = onVoteNo,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("👎 No")
                    }
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        },
        dismissButton = {
            if (!hasVoted) {
                TextButton(onClick = onDismiss) {
                    Text("Skip")
                }
            }
        }
    )
}
