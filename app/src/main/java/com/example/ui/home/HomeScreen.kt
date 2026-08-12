package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PostEntity
import com.example.data.model.GenreTheme
import com.example.data.model.MysteryStatus
import com.example.data.model.PostType
import com.example.ui.Screen

@Composable
fun HomeScreen(
    posts: List<PostEntity>,
    selectedFeedTab: String,
    onSelectFeedTab: (String) -> Unit,
    selectedGenreTheme: GenreTheme,
    onSelectGenreTheme: (GenreTheme) -> Unit,
    isReadingMode: Boolean,
    onToggleReadingMode: () -> Unit,
    isVerticalFeedMode: Boolean,
    onToggleVerticalFeedMode: () -> Unit,
    playingAudioPostId: String?,
    isPlayingAudio: Boolean,
    onPlayPauseAudio: (String) -> Unit,
    onToggleLike: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    var genreMenuExpanded by remember { mutableStateOf(false) }

    val feedTabs = listOf("For You", "Mysteries", "Real Incidents", "Stories", "Voice", "Opinions")

    val filteredPosts = remember(posts, selectedFeedTab) {
        when (selectedFeedTab) {
            "Mysteries" -> posts.filter { it.postType == PostType.MYSTERY }
            "Real Incidents" -> posts.filter { it.postType == PostType.REAL_INCIDENT }
            "Stories" -> posts.filter { it.postType == PostType.STORY }
            "Voice" -> posts.filter { it.isAudio }
            "Opinions" -> posts.filter { it.postType == PostType.OPINION }
            else -> posts
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onNavigateToOnboarding() }
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = "Verify",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "OUTPUTS",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "18+ Anonymous Platform",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Vertical TikTok story feed toggle
                        IconButton(
                            onClick = onToggleVerticalFeedMode,
                            modifier = Modifier.testTag("toggle_vertical_feed_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SwapVert,
                                contentDescription = "Vertical Feed",
                                tint = if (isVerticalFeedMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Reading Mode toggle
                        IconButton(
                            onClick = onToggleReadingMode,
                            modifier = Modifier.testTag("toggle_reading_mode_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MenuBook,
                                contentDescription = "Reading Mode",
                                tint = if (isReadingMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Genre Theme dropdown
                        Box {
                            IconButton(onClick = { genreMenuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Filled.FilterList,
                                    contentDescription = "Genre Theme",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            DropdownMenu(
                                expanded = genreMenuExpanded,
                                onDismissRequest = { genreMenuExpanded = false }
                            ) {
                                Text(
                                    text = "Select Aesthetic Theme",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                GenreTheme.values().forEach { genre ->
                                    DropdownMenuItem(
                                        text = { Text(genre.displayName) },
                                        onClick = {
                                            onSelectGenreTheme(genre)
                                            genreMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        IconButton(onClick = onNavigateToSearch) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable Feed Tabs
                ScrollableTabRow(
                    selectedTabIndex = feedTabs.indexOf(selectedFeedTab).coerceAtLeast(0),
                    edgePadding = 16.dp,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    feedTabs.forEach { tab ->
                        val isSelected = tab == selectedFeedTab
                        Tab(
                            selected = isSelected,
                            onClick = { onSelectFeedTab(tab) },
                            modifier = Modifier.testTag("feed_tab_$tab"),
                            text = {
                                Text(
                                    text = tab,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        )
                    }
                }
            }
        }

        // Body Content: Vertical Feed vs Normal List
        if (isVerticalFeedMode) {
            VerticalFeedView(
                posts = filteredPosts,
                playingAudioPostId = playingAudioPostId,
                isPlayingAudio = isPlayingAudio,
                onPlayPauseAudio = onPlayPauseAudio,
                onToggleLike = onToggleLike,
                onToggleSave = onToggleSave,
                onNavigateToDetail = onNavigateToDetail
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
            ) {
                items(filteredPosts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        isReadingMode = isReadingMode,
                        isPlayingAudio = playingAudioPostId == post.id && isPlayingAudio,
                        onPlayPauseAudio = { onPlayPauseAudio(post.id) },
                        onToggleLike = { onToggleLike(post.id) },
                        onToggleSave = { onToggleSave(post.id) },
                        onClick = { onNavigateToDetail(post.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PostCard(
    post: PostEntity,
    isReadingMode: Boolean,
    isPlayingAudio: Boolean,
    onPlayPauseAudio: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleSave: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("post_card_${post.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isReadingMode) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isReadingMode) 0.dp else 2.dp),
        border = if (isReadingMode) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category Tag, Mystery Status, Identity Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = post.postType.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    if (post.mysteryStatus == MysteryStatus.SOLVED) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF2E7D32))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "SOLVED ✓",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else if (post.mysteryStatus == MysteryStatus.INVESTIGATING) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE65100))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "INVESTIGATING",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Text(
                    text = post.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Author Display Name & Identity Mode
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = post.authorDisplayName.take(1).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = post.authorDisplayName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Content Snippet
            Text(
                text = post.content,
                style = if (isReadingMode) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
                maxLines = if (isReadingMode) 10 else 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (post.isAudio) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clickable { onPlayPauseAudio() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isPlayingAudio) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                            contentDescription = "Play Voice",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isPlayingAudio) "Playing Voice Story..." else "Voice Note (${post.audioDuration ?: "00:45"})",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Actions & Case CTA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleLike,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${post.likesCount}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.commentsCount}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (post.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Investigate CTA button for mysteries/incidents
                if (post.postType == PostType.MYSTERY || post.postType == PostType.REAL_INCIDENT) {
                    OutlinedButton(
                        onClick = onClick,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Investigate Case", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalFeedView(
    posts: List<PostEntity>,
    playingAudioPostId: String?,
    isPlayingAudio: Boolean,
    onPlayPauseAudio: (String) -> Unit,
    onToggleLike: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    if (posts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No posts in this feed yet.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        items(posts, key = { "vert_${it.id}" }) { post ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
                    .clickable { onNavigateToDetail(post.id) },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = post.postType.displayName,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = post.authorDisplayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 8,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Bottom controls
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        if (post.isAudio) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                                    .clickable { onPlayPauseAudio(post.id) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (playingAudioPostId == post.id && isPlayingAudio) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                                        contentDescription = "Audio",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Listen Voice Story (${post.audioDuration ?: "00:45"})", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onToggleLike(post.id) }) {
                                    Icon(
                                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = "Like",
                                        tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text("${post.likesCount}", fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.width(16.dp))

                                IconButton(onClick = { onToggleSave(post.id) }) {
                                    Icon(
                                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = "Save",
                                        tint = if (post.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            OutlinedButton(onClick = { onNavigateToDetail(post.id) }) {
                                Text("Open Case File")
                            }
                        }
                    }
                }
            }
        }
    }
}
