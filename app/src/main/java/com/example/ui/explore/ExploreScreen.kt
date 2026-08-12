package com.example.ui.explore

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PostEntity
import com.example.data.model.MysteryStatus
import com.example.ui.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    viewModel: MainViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val allPosts by viewModel.allPosts.collectAsState()
    val solvedPosts by viewModel.solvedPosts.collectAsState()

    val hashtags = listOf("#Kathmandu", "#Mystery", "#History", "#RealIncident", "#Unexplained", "#VoiceStories", "#TechInvasion", "#ColdCases")

    val filteredPosts = rememberSearchPosts(allPosts, searchQuery)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "EXPLORE & ARCHIVE",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search stories, mysteries, clues...") },
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().testTag("explore_search_field"),
            shape = RoundedCornerShape(20.dp)
        )

        // Topics & Hashtags
        Text("Trending Topics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            hashtags.forEach { tag ->
                val isSelected = searchQuery.equals(tag, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                        .clickable {
                            if (isSelected) viewModel.setSearchQuery("") else viewModel.setSearchQuery(tag.removePrefix("#"))
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = tag,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Solved Case Archive Section
        if (searchQuery.isBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Archive, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Solved Case Archive", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(solvedPosts, key = { "solved_${it.id}" }) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetail(post.id) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(post.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("Solved by Community • ${post.category}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        } else {
            Text("Search Results (${filteredPosts.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredPosts, key = { "search_${it.id}" }) { post ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToDetail(post.id) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(post.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(post.content, maxLines = 2, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberSearchPosts(posts: List<PostEntity>, query: String): List<PostEntity> {
    if (query.isBlank()) return posts
    return posts.filter {
        it.title.contains(query, ignoreCase = true) ||
                it.content.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
    }
}
