package com.example.ui.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GenreTheme
import com.example.data.model.PostType
import com.example.data.model.PrivacyMode
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateOutputScreen(
    onPublish: (
        title: String,
        content: String,
        type: PostType,
        genre: GenreTheme,
        category: String,
        privacy: PrivacyMode,
        author: String,
        location: String?,
        isAudio: Boolean,
        audioDuration: String?,
        knownFactsJson: String,
        unknownFactsJson: String,
        timelineEventsJson: String
    ) -> Unit
) {
    var selectedType by remember { mutableStateOf(PostType.MYSTERY) }
    var selectedGenre by remember { mutableStateOf(GenreTheme.MYSTERY) }
    var selectedPrivacy by remember { mutableStateOf(PrivacyMode.ANONYMOUS_NUMBER) }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Unexplained") }
    var locationName by remember { mutableStateOf("") }

    // Audio Voice Recording State
    var isRecording by remember { mutableStateOf(false) }
    var hasRecordedAudio by remember { mutableStateOf(false) }
    var recordedDurationSeconds by remember { mutableStateOf(0) }

    // Known & Unknown Facts
    var knownFactInput by remember { mutableStateOf("") }
    val knownFacts = remember { mutableStateListOf<String>() }

    var unknownFactInput by remember { mutableStateOf("") }
    val unknownFacts = remember { mutableStateListOf<String>() }

    // Timeline Event inputs
    var timelineTime by remember { mutableStateOf("") }
    var timelineTitle by remember { mutableStateOf("") }
    val timelineEvents = remember { mutableStateListOf<Pair<String, String>>() }

    val anonymousCode = remember { (1000..9999).random() }

    val categories = listOf("Unexplained", "Real Incident", "Voice Stories", "Civic Privacy", "Historical Mysteries", "Technology", "Local Lore")
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "CREATE OUTPUT",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Step 1: Post Type Selection
        Text("What are you publishing?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PostType.values().forEach { pt ->
                val isSelected = selectedType == pt
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                        .clickable { selectedType = pt }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("type_chip_${pt.name}")
                ) {
                    Text(
                        text = pt.displayName,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Step 2: Privacy Identity Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Public Identity Mode", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                PrivacyMode.values().forEach { pm ->
                    val isSel = selectedPrivacy == pm
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPrivacy = pm }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(16.dp),
                            shape = CircleShape,
                            color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ) {}
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(pm.displayName, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }

        // Step 3: Main Fields
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Output Title") },
            placeholder = { Text("The Lights That Appeared Every Night...") },
            modifier = Modifier.fillMaxWidth().testTag("output_title_field"),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories.forEach { c ->
                    DropdownMenuItem(text = { Text(c) }, onClick = { category = c; categoryExpanded = false })
                }
            }
        }

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Story / Incident Description (Unlimited Text)") },
            modifier = Modifier.fillMaxWidth().height(160.dp).testTag("output_body_field"),
            shape = RoundedCornerShape(12.dp)
        )

        // Step 4: Voice Note Recording simulation
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Voice Story Recording", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (!isRecording) {
                                isRecording = true
                                recordedDurationSeconds = 45
                                hasRecordedAudio = true
                            } else {
                                isRecording = false
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(if (isRecording) Color.Red else MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .testTag("record_voice_btn")
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                            contentDescription = "Mic",
                            tint = if (isRecording) Color.White else MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (hasRecordedAudio) {
                        Text("Recorded Voice Note: 00:45 ✓", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    } else if (isRecording) {
                        Text("Recording in progress...", color = Color.Red, fontWeight = FontWeight.Bold)
                    } else {
                        Text("Tap mic to record audio message", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Step 5: Mystery / Incident Specifics (Facts & Timeline)
        if (selectedType == PostType.MYSTERY || selectedType == PostType.REAL_INCIDENT) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Case Facts & Timeline Builder", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = knownFactInput,
                            onValueChange = { knownFactInput = it },
                            placeholder = { Text("Add confirmed fact...") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            if (knownFactInput.isNotBlank()) {
                                knownFacts.add(knownFactInput)
                                knownFactInput = ""
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        }
                    }

                    knownFacts.forEach { f ->
                        Text("✓ $f", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2E7D32))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = unknownFactInput,
                            onValueChange = { unknownFactInput = it },
                            placeholder = { Text("Add unknown anomaly...") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            if (unknownFactInput.isNotBlank()) {
                                unknownFacts.add(unknownFactInput)
                                unknownFactInput = ""
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        }
                    }

                    unknownFacts.forEach { u ->
                        Text("? $u", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Publish Button
        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    val authorName = when (selectedPrivacy) {
                        PrivacyMode.REAL_PROFILE -> "ShadowWriter"
                        PrivacyMode.PSEUDONYM -> "ShadowWriter"
                        PrivacyMode.ANONYMOUS_USERNAME -> "AnonymousFox"
                        PrivacyMode.ANONYMOUS_NUMBER -> "Anonymous #$anonymousCode"
                        PrivacyMode.COMPLETELY_ANONYMOUS -> "Anonymous"
                    }

                    val kJson = JSONArray(knownFacts).toString()
                    val uJson = JSONArray(unknownFacts).toString()

                    val tArr = JSONArray()
                    timelineEvents.forEach { t ->
                        val obj = JSONObject()
                        obj.put("timeLabel", t.first)
                        obj.put("title", t.second)
                        obj.put("description", "")
                        tArr.put(obj)
                    }

                    onPublish(
                        title, content, selectedType, selectedGenre, category,
                        selectedPrivacy, authorName, locationName.ifBlank { null },
                        hasRecordedAudio, if (hasRecordedAudio) "00:45" else null,
                        kJson, uJson, tArr.toString()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("publish_output_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Publish Output", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
