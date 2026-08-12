package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.OutputsBottomNavigationBar
import com.example.ui.create.CreateOutputScreen
import com.example.ui.detail.PostDetailScreen
import com.example.ui.explore.ExploreScreen
import com.example.ui.groups.GroupDetailScreen
import com.example.ui.groups.GroupsScreen
import com.example.ui.home.HomeScreen
import com.example.ui.inbox.ChatDetailScreen
import com.example.ui.inbox.InboxScreen
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.theme.OutputsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val genreTheme by viewModel.selectedGenreTheme.collectAsState()
            val posts by viewModel.allPosts.collectAsState()
            val selectedFeedTab by viewModel.selectedFeedTab.collectAsState()
            val isReadingMode by viewModel.isReadingMode.collectAsState()
            val isVerticalFeedMode by viewModel.isVerticalFeedMode.collectAsState()
            val playingAudioPostId by viewModel.playingAudioPostId.collectAsState()
            val isPlayingAudio by viewModel.isPlayingAudio.collectAsState()
            val userProfile by viewModel.userProfile.collectAsState()

            OutputsTheme(genreTheme = genreTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentScreen !is Screen.Onboarding &&
                            currentScreen !is Screen.PostDetail &&
                            currentScreen !is Screen.GroupDetail &&
                            currentScreen !is Screen.ChatDetail
                        ) {
                            OutputsBottomNavigationBar(
                                currentScreen = currentScreen,
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (val screen = currentScreen) {
                            is Screen.Onboarding -> {
                                OnboardingScreen(
                                    currentProfile = userProfile,
                                    viewModel = viewModel,
                                    onCompleteOnboarding = { newProfile ->
                                        viewModel.saveProfile(newProfile)
                                        viewModel.navigateTo(Screen.Home)
                                    }
                                )
                            }
                            is Screen.Home -> {
                                HomeScreen(
                                    posts = posts,
                                    selectedFeedTab = selectedFeedTab,
                                    onSelectFeedTab = { viewModel.setFeedTab(it) },
                                    selectedGenreTheme = genreTheme,
                                    onSelectGenreTheme = { viewModel.setGenreTheme(it) },
                                    isReadingMode = isReadingMode,
                                    onToggleReadingMode = { viewModel.toggleReadingMode() },
                                    isVerticalFeedMode = isVerticalFeedMode,
                                    onToggleVerticalFeedMode = { viewModel.toggleVerticalFeedMode() },
                                    playingAudioPostId = playingAudioPostId,
                                    isPlayingAudio = isPlayingAudio,
                                    onPlayPauseAudio = { viewModel.playPauseAudio(it) },
                                    onToggleLike = { viewModel.toggleLikePost(it) },
                                    onToggleSave = { viewModel.toggleSavePost(it) },
                                    onNavigateToDetail = { viewModel.navigateTo(Screen.PostDetail(it)) },
                                    onNavigateToSearch = { viewModel.navigateTo(Screen.Explore) },
                                    onNavigateToOnboarding = { viewModel.navigateTo(Screen.Onboarding) }
                                )
                            }
                            is Screen.PostDetail -> {
                                PostDetailScreen(
                                    postId = screen.postId,
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(Screen.Home) }
                                )
                            }
                            is Screen.CreateOutput -> {
                                CreateOutputScreen(
                                    onPublish = { title, content, type, genre, category, privacy, author, location, isAudio, duration, kJson, uJson, tJson ->
                                        viewModel.createPost(
                                            title, content, type, genre, category, privacy, author, location, isAudio, duration, kJson, uJson, tJson
                                        )
                                    }
                                )
                            }
                            is Screen.Explore -> {
                                ExploreScreen(
                                    viewModel = viewModel,
                                    onNavigateToDetail = { viewModel.navigateTo(Screen.PostDetail(it)) }
                                )
                            }
                            is Screen.Groups -> {
                                GroupsScreen(
                                    viewModel = viewModel,
                                    onOpenGroupDetail = { viewModel.navigateTo(Screen.GroupDetail(it)) }
                                )
                            }
                            is Screen.GroupDetail -> {
                                GroupDetailScreen(
                                    groupId = screen.groupId,
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(Screen.Groups) }
                                )
                            }
                            is Screen.Inbox -> {
                                InboxScreen(
                                    viewModel = viewModel,
                                    onOpenChat = { convoId, partnerName ->
                                        viewModel.navigateTo(Screen.ChatDetail(convoId, partnerName))
                                    }
                                )
                            }
                            is Screen.ChatDetail -> {
                                ChatDetailScreen(
                                    conversationId = screen.conversationId,
                                    partnerName = screen.partnerName,
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(Screen.Inbox) }
                                )
                            }
                            is Screen.Profile -> {
                                ProfileScreen(
                                    viewModel = viewModel,
                                    onNavigateToDetail = { viewModel.navigateTo(Screen.PostDetail(it)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
