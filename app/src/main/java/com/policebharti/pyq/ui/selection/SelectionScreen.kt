package com.policebharti.pyq.ui.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.policebharti.pyq.ui.theme.*

/**
 * Content Selection Screen
 * ─ District → Year → Paper cascading dropdowns.
 * ─ Shows download status and pack expiry info.
 * ─ Premium card-based layout with gradient accents.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionScreen(
    onStartTest: (district: String, year: String, set: String) -> Unit,
    onViewPausedTests: () -> Unit,
    onViewBookmarks: () -> Unit
) {
    var selectedDistrict by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var selectedSet by remember { mutableStateOf("") }
    var districtExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    var setExpanded by remember { mutableStateOf(false) }

    val districts = listOf(
        "Mumbai", "Pune", "Nagpur", "Nashik", "Aurangabad", "Solapur",
        "Kolhapur", "Thane", "Ratnagiri", "Satara", "Sangli", "Jalgaon",
        "Ahmednagar", "Amravati", "Akola", "Latur", "Nanded"
    )
    val years = listOf("2024", "2023", "2022", "2021", "2020", "2019", "2018")
    val sets = listOf("Set A", "Set B", "Set C", "Set D")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Select Paper",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GradientStart,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onViewBookmarks) {
                        Icon(Icons.Default.Bookmark, "Bookmarks", tint = AccentGold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Step indicator ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StepChip("1. District", selectedDistrict.isNotEmpty())
                    StepChip("2. Year", selectedYear.isNotEmpty())
                    StepChip("3. Paper", selectedSet.isNotEmpty())
                }
            }

            // ── District dropdown ──
            Text("📍 Select District", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            ExposedDropdownMenuBox(
                expanded = districtExpanded,
                onExpandedChange = { districtExpanded = !districtExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDistrict,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("District") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(districtExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = districtExpanded,
                    onDismissRequest = { districtExpanded = false }
                ) {
                    districts.forEach { d ->
                        DropdownMenuItem(
                            text = { Text(d) },
                            onClick = {
                                selectedDistrict = d
                                districtExpanded = false
                                selectedYear = ""
                                selectedSet = ""
                            }
                        )
                    }
                }
            }

            // ── Year dropdown (enabled only after district) ──
            if (selectedDistrict.isNotEmpty()) {
                Text("📅 Select Year", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = { yearExpanded = !yearExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedYear,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Year") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(yearExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        years.forEach { y ->
                            DropdownMenuItem(
                                text = { Text(y) },
                                onClick = {
                                    selectedYear = y
                                    yearExpanded = false
                                    selectedSet = ""
                                }
                            )
                        }
                    }
                }
            }

            // ── Set dropdown (enabled only after year) ──
            if (selectedYear.isNotEmpty()) {
                Text("📄 Select Paper Set", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                ExposedDropdownMenuBox(
                    expanded = setExpanded,
                    onExpandedChange = { setExpanded = !setExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSet,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Paper Set") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(setExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = setExpanded,
                        onDismissRequest = { setExpanded = false }
                    ) {
                        sets.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s) },
                                onClick = {
                                    selectedSet = s
                                    setExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Start Test button ──
            Button(
                onClick = { onStartTest(selectedDistrict, selectedYear, selectedSet) },
                enabled = selectedDistrict.isNotEmpty() && selectedYear.isNotEmpty() && selectedSet.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Test", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // ── Paused Tests shortcut ──
            OutlinedButton(
                onClick = onViewPausedTests,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Pause, contentDescription = null, tint = AccentAmber)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Resume Paused Tests", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun StepChip(label: String, isComplete: Boolean) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isComplete) CorrectGreen.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isComplete) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = CorrectGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}
