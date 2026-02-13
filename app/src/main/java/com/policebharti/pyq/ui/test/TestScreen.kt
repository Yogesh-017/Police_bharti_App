package com.policebharti.pyq.ui.test

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.policebharti.pyq.ui.theme.*

/**
 * Test Screen — the core exam experience.
 * ─ 90-minute countdown timer (visible top bar).
 * ─ MCQ question with 4 options.
 * ─ Question navigation palette (color-coded).
 * ─ Pause, Bookmark, and Submit controls.
 * ─ Color codes: Green=correct, Amber=answered, Grey=not visited.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    district: String = "",
    year: String = "",
    setName: String = "",
    onPause: () -> Unit = {},
    onSubmit: () -> Unit = {},
    onShowAiHelp: () -> Unit = {}
) {
    // ── Demo state (replace with ViewModel in production) ──
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf("") }
    var showPalette by remember { mutableStateOf(false) }
    var showSubmitDialog by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    // Demo timer display
    var remainingMs by remember { mutableLongStateOf(90 * 60 * 1000L) }
    val timerText = "%02d:%02d".format(remainingMs / 60000, (remainingMs % 60000) / 1000)

    // Demo questions
    val totalQuestions = 100
    val demoQuestion = "महाराष्ट्र राज्याची स्थापना कोणत्या वर्षी झाली?\n\nIn which year was the state of Maharashtra established?"
    val options = listOf(
        "A" to "1950",
        "B" to "1956",
        "C" to "1960",
        "D" to "1962"
    )
    // Track answer status per question: NOT_VISITED, VISITED, ANSWERED
    val answerStatuses = remember { mutableStateListOf<String>().apply { repeat(totalQuestions) { add("NOT_VISITED") } } }

    // Mark current as visited
    LaunchedEffect(currentIndex) {
        if (answerStatuses[currentIndex] == "NOT_VISITED") {
            answerStatuses[currentIndex] = "VISITED"
        }
    }

    Scaffold(
        topBar = {
            // ── Timer Bar ──
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = AccentGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = timerText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (remainingMs < 5 * 60 * 1000L) WrongRed else Color.White
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Q ${currentIndex + 1}/$totalQuestions",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    // Bookmark button
                    IconButton(onClick = { isBookmarked = !isBookmarked }) {
                        Icon(
                            if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = AccentGold
                        )
                    }
                    // AI Help button (locked)
                    IconButton(onClick = onShowAiHelp) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI Help", tint = NotVisitedGrey)
                    }
                    // Palette toggle
                    IconButton(onClick = { showPalette = !showPalette }) {
                        Icon(Icons.Default.GridView, contentDescription = "Palette", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GradientStart,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // ── Bottom Navigation ──
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pause button
                    OutlinedButton(
                        onClick = onPause,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pause", fontSize = 13.sp)
                    }

                    // Prev / Next
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalButton(
                            onClick = { if (currentIndex > 0) { selectedOption = ""; currentIndex-- } },
                            enabled = currentIndex > 0,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Prev")
                        }
                        FilledTonalButton(
                            onClick = { if (currentIndex < totalQuestions - 1) { selectedOption = ""; currentIndex++ } },
                            enabled = currentIndex < totalQuestions - 1,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Next")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next", modifier = Modifier.size(18.dp))
                        }
                    }

                    // Submit button
                    Button(
                        onClick = { showSubmitDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CorrectGreen)
                    ) {
                        Text("Submit", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Main question content ──
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Question number badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RoyalBlue.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "Question ${currentIndex + 1}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = RoyalBlue
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Question text
                Text(
                    text = demoQuestion,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Options
                options.forEach { (label, text) ->
                    val isSelected = selectedOption == label
                    Card(
                        onClick = {
                            selectedOption = label
                            answerStatuses[currentIndex] = "ANSWERED"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) RoyalBlue else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) RoyalBlue.copy(alpha = 0.08f)
                                             else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 0.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Option label circle
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = if (isSelected) RoyalBlue else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = text,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = RoyalBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ── Question Palette Overlay ──
            if (showPalette) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.97f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Question Palette", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            IconButton(onClick = { showPalette = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                        // Legend
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            LegendDot(color = CorrectGreen, label = "Current")
                            LegendDot(color = AnsweredAmber, label = "Answered")
                            LegendDot(color = NotVisitedGrey, label = "Not Visited")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(8),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            itemsIndexed(answerStatuses) { index, status ->
                                val bgColor = when {
                                    index == currentIndex -> CorrectGreen
                                    status == "ANSWERED" -> AnsweredAmber
                                    status == "VISITED" -> RoyalBlue.copy(alpha = 0.3f)
                                    else -> NotVisitedGrey.copy(alpha = 0.3f)
                                }
                                Surface(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            currentIndex = index
                                            selectedOption = ""
                                            showPalette = false
                                        },
                                    color = bgColor
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            textAlign = TextAlign.Center
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

    // ── Submit Confirmation Dialog ──
    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            icon = { Icon(Icons.Default.Assignment, contentDescription = null, tint = RoyalBlue) },
            title = { Text("Submit Test?", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    val answered = answerStatuses.count { it == "ANSWERED" }
                    val notVisited = answerStatuses.count { it == "NOT_VISITED" }
                    Text("Answered: $answered / $totalQuestions")
                    Text("Not Visited: $notVisited")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Are you sure you want to submit? You cannot change answers after submission.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        onSubmit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CorrectGreen)
                ) {
                    Text("Submit", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(12.dp),
            shape = CircleShape,
            color = color
        ) {}
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    }
}
