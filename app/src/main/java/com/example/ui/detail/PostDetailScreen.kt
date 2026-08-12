package com.example.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.CommentEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.PostEntity
import com.example.data.local.TheoryEntity
import com.example.data.model.EvidenceType
import com.example.data.model.MysteryStatus
import com.example.data.model.PostType
import com.example.data.model.PrivacyMode
import com.example.ui.MainViewModel
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val posts by viewModel.allPosts.collectAsState()
    val post = posts.find { it.id == postId }

    if (post == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Case File not found.")
        }
        return
    }

    val comments by viewModel.getCommentsForPost(postId).collectAsState()
    val evidenceList by viewModel.getEvidenceForPost(postId).collectAsState()
    val theoriesList by viewModel.getTheoriesForPost(postId).collectAsState()

    var activeTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Timeline, 2: Evidence, 3: Theories, 4: Discussion
    val tabs = listOf("Overview", "Timeline", "Evidence (${evidenceList.size})", "Theories (${theoriesList.size})", "Discussion (${comments.size})")

    var showAddEvidenceDialog by remember { mutableStateOf(false) }
    var showAddTheoryDialog by remember { mutableStateOf(false) }
    var commentInput by remember { mutableStateOf("") }

    val playingAudioPostId by viewModel.playingAudioPostId.collectAsState()
    val isPlayingAudio by viewModel.isPlayingAudio.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = post.postType.displayName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_btn")) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleSavePost(post.id) }) {
                        Icon(
                            imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (post.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Case Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = activeTab,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = { Text(title, fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    0 -> OverviewTab(
                        post = post,
                        isPlayingAudio = playingAudioPostId == post.id && isPlayingAudio,
                        onPlayPauseAudio = { viewModel.playPauseAudio(post.id) },
                        onToggleLike = { viewModel.toggleLikePost(post.id) }
                    )
                    1 -> TimelineTab(post = post)
                    2 -> EvidenceTab(
                        evidenceList = evidenceList,
                        onAddEvidence = { showAddEvidenceDialog = true }
                    )
                    3 -> TheoriesTab(
                        theoriesList = theoriesList,
                        onAddTheory = { showAddTheoryDialog = true },
                        onUpvoteTheory = { viewModel.toggleTheoryUpvote(it) },
                        onMarkAsSolution = { theoryId -> viewModel.markTheoryAsSolution(post.id, theoryId) }
                    )
                    4 -> DiscussionTab(
                        comments = comments,
                        commentInput = commentInput,
                        onCommentInputChange = { commentInput = it },
                        onSendComment = {
                            if (commentInput.isNotBlank()) {
                                viewModel.addComment(post.id, commentInput, "Me (Anon #7294)", PrivacyMode.ANONYMOUS_NUMBER)
                                commentInput = ""
                            }
                        }
                    )
                }
            }
        }
    }

    // Add Evidence Dialog
    if (showAddEvidenceDialog) {
        AddEvidenceDialog(
            onDismiss = { showAddEvidenceDialog = false },
            onSubmit = { title, desc, type, source, confidence ->
                viewModel.addEvidence(post.id, title, desc, type, source, "Me (Anon #7294)", confidence)
                showAddEvidenceDialog = false
            }
        )
    }

    // Add Theory Dialog
    if (showAddTheoryDialog) {
        AddTheoryDialog(
            onDismiss = { showAddTheoryDialog = false },
            onSubmit = { title, desc ->
                viewModel.addTheory(post.id, title, desc, "AnonInvestigator #7294", PrivacyMode.ANONYMOUS_NUMBER)
                showAddTheoryDialog = false
            }
        )
    }
}

@Composable
fun OverviewTab(
    post: PostEntity,
    isPlayingAudio: Boolean,
    onPlayPauseAudio: () -> Unit,
    onToggleLike: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Title & Author Meta
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = post.postType.displayName,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        if (post.mysteryStatus == MysteryStatus.SOLVED) {
                            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF2E7D32)) {
                                Text("SOLVED CASE ✓", color = Color.White, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(8.dp, 4.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = post.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Posted by ${post.authorDisplayName} • ${post.timestamp} • Category: ${post.category}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (post.isAudio) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPlayPauseAudio() }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isPlayingAudio) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                                    contentDescription = "Play",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isPlayingAudio) "Playing Audio Story..." else "Play Audio Recording (${post.audioDuration ?: "03:14"})",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // Main Body Description
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Case Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = post.content, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
                }
            }
        }

        // Known & Unknown Facts
        item {
            val knownFacts = parseJsonList(post.knownFactsJson)
            val unknownFacts = parseJsonList(post.unknownFactsJson)

            if (knownFacts.isNotEmpty() || unknownFacts.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (knownFacts.isNotEmpty()) {
                            Text("Confirmed Facts", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            knownFacts.forEach { fact ->
                                Text("• $fact", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 2.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (unknownFacts.isNotEmpty()) {
                            Text("Unexplained Anomalies", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                            unknownFacts.forEach { anomaly ->
                                Text("• $anomaly", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineTab(post: PostEntity) {
    val timelineEvents = remember(post.timelineEventsJson) {
        val list = mutableListOf<Triple<String, String, String>>()
        runCatching {
            val arr = JSONArray(post.timelineEventsJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(Triple(obj.optString("timeLabel"), obj.optString("title"), obj.optString("description")))
            }
        }
        list
    }

    if (timelineEvents.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No timeline events logged for this case.", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(timelineEvents) { event ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp)
                    ) {
                        Text(event.first, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(event.second, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(event.third, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun EvidenceTab(
    evidenceList: List<EvidenceEntity>,
    onAddEvidence: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Evidence Board", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Button(onClick = onAddEvidence, shape = RoundedCornerShape(12.dp), modifier = Modifier.testTag("submit_evidence_btn")) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Submit Evidence")
            }
        }

        if (evidenceList.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No evidence items submitted yet. Be the first to contribute!")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(evidenceList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                                    Text(item.type.displayName, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(6.dp, 2.dp), fontWeight = FontWeight.Bold)
                                }
                                Text("Confidence: ${item.confidenceLevel}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(item.description, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Source: ${item.source} • Contributor: ${item.contributor}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TheoriesTab(
    theoriesList: List<TheoryEntity>,
    onAddTheory: () -> Unit,
    onUpvoteTheory: (String) -> Unit,
    onMarkAsSolution: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Community Theories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Button(onClick = onAddTheory, shape = RoundedCornerShape(12.dp), modifier = Modifier.testTag("submit_theory_btn")) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Propose Theory")
            }
        }

        if (theoriesList.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No theories submitted yet. Propose your explanation!")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(theoriesList) { theory ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (theory.status == "Accepted Solution") Color(0xFF1B3820) else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (theory.status == "Accepted Solution") {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF2E7D32)) {
                                    Text("✓ ACCEPTED SOLUTION", color = Color.White, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(6.dp, 2.dp), fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Text(theory.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(theory.description, style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("By ${theory.authorIdentity}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { onUpvoteTheory(theory.id) }) {
                                        Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = "Vote", tint = if (theory.isUpvoted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text("${theory.votesCount}", style = MaterialTheme.typography.labelMedium)

                                    if (theory.status != "Accepted Solution") {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        TextButton(onClick = { onMarkAsSolution(theory.id) }) {
                                            Text("Accept Solution", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiscussionTab(
    comments: List<CommentEntity>,
    commentInput: String,
    onCommentInputChange: (String) -> Unit,
    onSendComment: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(comments) { comment ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(comment.authorDisplayName, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Text(comment.timestamp, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(comment.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Input bar
        Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 4.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentInput,
                    onValueChange = onCommentInputChange,
                    placeholder = { Text("Add to discussion...") },
                    modifier = Modifier.weight(1f).testTag("comment_input_field"),
                    shape = RoundedCornerShape(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onSendComment, modifier = Modifier.testTag("send_comment_btn")) {
                    Icon(imageVector = Icons.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEvidenceDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, EvidenceType, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(EvidenceType.PHOTOGRAPH) }
    var confidence by remember { mutableStateOf("Medium") }
    var typeExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Submit Evidence Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Evidence Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = source, onValueChange = { source = it }, label = { Text("Source / Provenance") }, modifier = Modifier.fillMaxWidth())

                ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = !typeExpanded }) {
                    OutlinedTextField(
                        value = type.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Evidence Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                        EvidenceType.values().forEach { et ->
                            DropdownMenuItem(text = { Text(et.displayName) }, onClick = { type = et; typeExpanded = false })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onSubmit(title, desc, type, source, confidence) }) {
                Text("Add to Case File")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddTheoryDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Propose Theory") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Theory Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Detailed Explanation") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            }
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onSubmit(title, desc) }) {
                Text("Publish Theory")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun parseJsonList(jsonStr: String): List<String> {
    val list = mutableListOf<String>()
    runCatching {
        val arr = JSONArray(jsonStr)
        for (i in 0 until arr.length()) {
            list.add(arr.getString(i))
        }
    }
    return list
}
