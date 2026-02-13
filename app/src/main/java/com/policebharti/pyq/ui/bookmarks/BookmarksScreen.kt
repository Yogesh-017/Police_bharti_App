package com.policebharti.pyq.ui.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.policebharti.pyq.ui.theme.*

/**
 * Bookmarks Screen
 * ─ Shows all bookmarked questions in a list.
 * ─ Each card displays question text, district/year, and correct answer.
 * ─ Swipe or button to remove bookmark.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    onBack: () -> Unit = {},
    onQuestionTap: (questionId: Long) -> Unit = {}
) {
    // Demo data (replace with ViewModel + Flow)
    data class BookmarkItem(
        val id: Long, val questionNo: Int, val questionText: String,
        val correctOption: String, val district: String, val year: String
    )
    val bookmarks = remember {
        mutableStateListOf(
            BookmarkItem(1, 12, "महाराष्ट्राचे पहिले मुख्यमंत्री कोण होते?", "B", "Pune", "2023"),
            BookmarkItem(2, 45, "भारतीय संविधानाचे कलम 370 कशाशी संबंधित आहे?", "C", "Mumbai", "2022"),
            BookmarkItem(3, 78, "संयुक्त राष्ट्र संघटनेची स्थापना कोणत्या वर्षी झाली?", "A", "Nagpur", "2024")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GradientStart,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    Text(
                        "${bookmarks.size} saved",
                        color = AccentGold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { padding ->
        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = NotVisitedGrey,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "No bookmarks yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        "Tap the bookmark icon during a test to save questions.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bookmarks, key = { it.id }) { bm ->
                    Card(
                        onClick = { onQuestionTap(bm.id) },
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                // Question badge
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = RoyalBlue.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        "Q${bm.questionNo}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalBlue
                                    )
                                }
                                // Remove bookmark
                                IconButton(
                                    onClick = { bookmarks.remove(bm) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.BookmarkRemove,
                                        contentDescription = "Remove",
                                        tint = WrongRed.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = bm.questionText,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                maxLines = 3
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "${bm.district} • ${bm.year}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CorrectGreen.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        "Ans: ${bm.correctOption}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CorrectGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
