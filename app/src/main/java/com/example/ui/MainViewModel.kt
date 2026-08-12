package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthState
import com.example.data.auth.FirebaseAuthManager
import com.example.data.local.CommentEntity
import com.example.data.local.DirectMessageEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.GroupEntity
import com.example.data.local.GroupMessageEntity
import com.example.data.local.OutputsDatabase
import com.example.data.local.PostEntity
import com.example.data.local.TheoryEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.EvidenceType
import com.example.data.model.GenreTheme
import com.example.data.model.MysteryStatus
import com.example.data.model.PostType
import com.example.data.model.PrivacyMode
import com.example.data.repository.OutputsRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Onboarding : Screen()
    object Home : Screen()
    data class PostDetail(val postId: String) : Screen()
    object CreateOutput : Screen()
    object Explore : Screen()
    object Groups : Screen()
    data class GroupDetail(val groupId: String) : Screen()
    object Inbox : Screen()
    data class ChatDetail(val conversationId: String, val partnerName: String) : Screen()
    object Profile : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = OutputsDatabase.getDatabase(application)
    private val repository = OutputsRepository(db.outputsDao())
    val authManager = FirebaseAuthManager(application)

    val firebaseUser: StateFlow<FirebaseUser?> = authManager.currentUser
    val authState: StateFlow<AuthState> = authManager.authState

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedGenreTheme = MutableStateFlow(GenreTheme.DEFAULT)
    val selectedGenreTheme: StateFlow<GenreTheme> = _selectedGenreTheme.asStateFlow()

    private val _isReadingMode = MutableStateFlow(false)
    val isReadingMode: StateFlow<Boolean> = _isReadingMode.asStateFlow()

    private val _isVerticalFeedMode = MutableStateFlow(false)
    val isVerticalFeedMode: StateFlow<Boolean> = _isVerticalFeedMode.asStateFlow()

    private val _selectedFeedTab = MutableStateFlow("For You")
    val selectedFeedTab: StateFlow<String> = _selectedFeedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Currently playing audio story
    private val _playingAudioPostId = MutableStateFlow<String?>(null)
    val playingAudioPostId: StateFlow<String?> = _playingAudioPostId.asStateFlow()

    private val _isPlayingAudio = MutableStateFlow(false)
    val isPlayingAudio: StateFlow<Boolean> = _isPlayingAudio.asStateFlow()

    val allPosts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPosts: StateFlow<List<PostEntity>> = repository.savedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val solvedPosts: StateFlow<List<PostEntity>> = repository.solvedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGroups: StateFlow<List<GroupEntity>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val directMessages: StateFlow<List<DirectMessageEntity>> = repository.allDirectMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun signInWithEmail(email: String, pass: String, onResult: (Result<FirebaseUser>) -> Unit = {}) {
        viewModelScope.launch {
            val result = authManager.signInWithEmail(email, pass)
            onResult(result)
        }
    }

    fun signUpWithEmail(email: String, pass: String, onResult: (Result<FirebaseUser>) -> Unit = {}) {
        viewModelScope.launch {
            val result = authManager.signUpWithEmail(email, pass)
            onResult(result)
        }
    }

    fun signInAnonymously(onResult: (Result<FirebaseUser>) -> Unit = {}) {
        viewModelScope.launch {
            val result = authManager.signInAnonymously()
            onResult(result)
        }
    }

    fun signOutFirebase() {
        authManager.signOut()
    }

    fun resetAuthState() {
        authManager.resetAuthState()
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setGenreTheme(theme: GenreTheme) {
        _selectedGenreTheme.value = theme
    }

    fun toggleReadingMode() {
        _isReadingMode.value = !_isReadingMode.value
    }

    fun toggleVerticalFeedMode() {
        _isVerticalFeedMode.value = !_isVerticalFeedMode.value
    }

    fun setFeedTab(tab: String) {
        _selectedFeedTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun playPauseAudio(postId: String) {
        if (_playingAudioPostId.value == postId) {
            _isPlayingAudio.value = !_isPlayingAudio.value
        } else {
            _playingAudioPostId.value = postId
            _isPlayingAudio.value = true
        }
    }

    fun stopAudio() {
        _playingAudioPostId.value = null
        _isPlayingAudio.value = false
    }

    fun toggleLikePost(postId: String) {
        viewModelScope.launch {
            repository.toggleLikePost(postId)
        }
    }

    fun toggleSavePost(postId: String) {
        viewModelScope.launch {
            repository.toggleSavePost(postId)
        }
    }

    fun createPost(
        title: String,
        content: String,
        postType: PostType,
        genreTheme: GenreTheme,
        category: String,
        privacyMode: PrivacyMode,
        authorName: String,
        locationName: String? = null,
        isAudio: Boolean = false,
        audioDuration: String? = null,
        knownFactsJson: String = "[]",
        unknownFactsJson: String = "[]",
        timelineEventsJson: String = "[]"
    ) {
        viewModelScope.launch {
            repository.createPost(
                title, content, postType, genreTheme, category, privacyMode,
                authorName, locationName, isAudio, audioDuration,
                knownFactsJson, unknownFactsJson, timelineEventsJson
            )
            _currentScreen.value = Screen.Home
        }
    }

    fun addComment(postId: String, content: String, authorName: String, privacyMode: PrivacyMode) {
        viewModelScope.launch {
            repository.addComment(postId, content, authorName, privacyMode)
        }
    }

    fun addEvidence(
        postId: String,
        title: String,
        description: String,
        type: EvidenceType,
        source: String,
        contributor: String,
        confidenceLevel: String
    ) {
        viewModelScope.launch {
            repository.addEvidence(postId, title, description, type, source, contributor, confidenceLevel)
        }
    }

    fun addTheory(
        postId: String,
        title: String,
        description: String,
        authorIdentity: String,
        privacyMode: PrivacyMode
    ) {
        viewModelScope.launch {
            repository.addTheory(postId, title, description, authorIdentity, privacyMode)
        }
    }

    fun toggleTheoryUpvote(theoryId: String) {
        viewModelScope.launch {
            repository.toggleTheoryUpvote(theoryId)
        }
    }

    fun markTheoryAsSolution(postId: String, theoryId: String) {
        viewModelScope.launch {
            repository.markTheoryAsSolution(postId, theoryId)
        }
    }

    fun toggleJoinGroup(groupId: String) {
        viewModelScope.launch {
            repository.toggleJoinGroup(groupId)
        }
    }

    fun sendGroupMessage(groupId: String, content: String, senderIdentity: String) {
        viewModelScope.launch {
            repository.sendGroupMessage(groupId, content, senderIdentity)
        }
    }

    fun sendDirectMessage(conversationId: String, content: String) {
        viewModelScope.launch {
            repository.sendDirectMessage(conversationId, content)
        }
    }

    fun saveProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }

    fun getCommentsForPost(postId: String): StateFlow<List<CommentEntity>> =
        repository.getCommentsForPost(postId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getEvidenceForPost(postId: String): StateFlow<List<EvidenceEntity>> =
        repository.getEvidenceForPost(postId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getTheoriesForPost(postId: String): StateFlow<List<TheoryEntity>> =
        repository.getTheoriesForPost(postId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getGroupMessages(groupId: String): StateFlow<List<GroupMessageEntity>> =
        repository.getGroupMessages(groupId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
