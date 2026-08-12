package com.example.data.repository

import com.example.data.local.CommentEntity
import com.example.data.local.DirectMessageEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.GroupEntity
import com.example.data.local.GroupMessageEntity
import com.example.data.local.OutputsDao
import com.example.data.local.PostEntity
import com.example.data.local.SeedData
import com.example.data.local.TheoryEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.EvidenceType
import com.example.data.model.GenreTheme
import com.example.data.model.MysteryStatus
import com.example.data.model.PostType
import com.example.data.model.PrivacyMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class OutputsRepository(private val dao: OutputsDao) {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val posts = dao.getAllPosts().firstOrNull()
        if (posts.isNullOrEmpty()) {
            dao.insertPosts(SeedData.initialPosts)
            dao.insertComments(SeedData.initialComments)
            dao.insertEvidenceList(SeedData.initialEvidence)
            dao.insertTheories(SeedData.initialTheories)
            dao.insertGroups(SeedData.initialGroups)
            dao.insertGroupMessages(SeedData.initialGroupMessages)
            dao.insertDirectMessages(SeedData.initialDirectMessages)
            dao.saveUserProfile(SeedData.initialProfile)
        }
    }

    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val savedPosts: Flow<List<PostEntity>> = dao.getSavedPosts()
    val solvedPosts: Flow<List<PostEntity>> = dao.getSolvedPosts()
    val allGroups: Flow<List<GroupEntity>> = dao.getAllGroups()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val allDirectMessages: Flow<List<DirectMessageEntity>> = dao.getAllDirectMessages()

    fun getPostById(postId: String): Flow<PostEntity?> = dao.getPostById(postId)
    fun getCommentsForPost(postId: String): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)
    fun getEvidenceForPost(postId: String): Flow<List<EvidenceEntity>> = dao.getEvidenceForPost(postId)
    fun getTheoriesForPost(postId: String): Flow<List<TheoryEntity>> = dao.getTheoriesForPost(postId)
    fun getGroupMessages(groupId: String): Flow<List<GroupMessageEntity>> = dao.getGroupMessages(groupId)

    suspend fun createPost(
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
        val newPost = PostEntity(
            id = "post_${System.currentTimeMillis()}",
            title = title,
            content = content,
            postType = postType,
            genreTheme = genreTheme,
            mysteryStatus = if (postType == PostType.MYSTERY || postType == PostType.REAL_INCIDENT) MysteryStatus.OPEN else MysteryStatus.OPEN,
            privacyMode = privacyMode,
            authorDisplayName = authorName,
            timestamp = "Just now",
            category = category,
            views = 1,
            likesCount = 0,
            commentsCount = 0,
            savesCount = 0,
            isSaved = false,
            isLiked = false,
            isAudio = isAudio,
            audioUrl = if (isAudio) "user_recorded_audio.mp3" else null,
            audioDuration = audioDuration,
            locationName = locationName,
            knownFactsJson = knownFactsJson,
            unknownFactsJson = unknownFactsJson,
            timelineEventsJson = timelineEventsJson
        )
        dao.insertPost(newPost)
    }

    suspend fun addComment(
        postId: String,
        content: String,
        authorName: String,
        privacyMode: PrivacyMode
    ) {
        val comment = CommentEntity(
            id = "c_${System.currentTimeMillis()}",
            postId = postId,
            authorDisplayName = authorName,
            privacyMode = privacyMode,
            content = content,
            timestamp = "Just now"
        )
        dao.insertComment(comment)
        
        // Update comments count on post
        val currentPost = dao.getPostById(postId).firstOrNull()
        if (currentPost != null) {
            dao.updatePost(currentPost.copy(commentsCount = currentPost.commentsCount + 1))
        }
    }

    suspend fun addEvidence(
        postId: String,
        title: String,
        description: String,
        type: EvidenceType,
        source: String,
        contributor: String,
        confidenceLevel: String
    ) {
        val evidence = EvidenceEntity(
            id = "e_${System.currentTimeMillis()}",
            postId = postId,
            type = type,
            title = title,
            description = description,
            source = source,
            contributor = contributor,
            confidenceLevel = confidenceLevel,
            dateAdded = "Just now"
        )
        dao.insertEvidence(evidence)
    }

    suspend fun addTheory(
        postId: String,
        title: String,
        description: String,
        authorIdentity: String,
        privacyMode: PrivacyMode
    ) {
        val theory = TheoryEntity(
            id = "theory_${System.currentTimeMillis()}",
            postId = postId,
            authorIdentity = authorIdentity,
            authorMode = privacyMode,
            title = title,
            description = description,
            votesCount = 1,
            status = "Under Review",
            isUpvoted = true
        )
        dao.insertTheory(theory)
    }

    suspend fun toggleLikePost(postId: String) = dao.toggleLikePost(postId)
    suspend fun toggleSavePost(postId: String) = dao.toggleSavePost(postId)
    suspend fun toggleTheoryUpvote(theoryId: String) = dao.toggleTheoryUpvote(theoryId)
    suspend fun updateMysteryStatus(postId: String, status: MysteryStatus) = dao.updateMysteryStatus(postId, status.name)
    suspend fun markTheoryAsSolution(postId: String, theoryId: String) {
        dao.updateTheoryStatus(theoryId, "Accepted Solution")
        val post = dao.getPostById(postId).firstOrNull()
        if (post != null) {
            dao.updatePost(post.copy(mysteryStatus = MysteryStatus.SOLVED, acceptedAnswerId = theoryId))
        }
    }

    suspend fun toggleJoinGroup(groupId: String) = dao.toggleJoinGroup(groupId)

    suspend fun sendGroupMessage(groupId: String, content: String, senderIdentity: String) {
        val msg = GroupMessageEntity(
            id = "gm_${System.currentTimeMillis()}",
            groupId = groupId,
            senderIdentity = senderIdentity,
            content = content,
            timestamp = "Just now"
        )
        dao.insertGroupMessage(msg)
    }

    suspend fun sendDirectMessage(conversationId: String, content: String, isRequest: Boolean = false) {
        val msg = DirectMessageEntity(
            id = "dm_${System.currentTimeMillis()}",
            conversationId = conversationId,
            senderId = "self",
            senderDisplayName = "Me (Anon #7294)",
            content = content,
            timestamp = "Just now",
            isRequest = isRequest
        )
        dao.insertDirectMessage(msg)
    }

    suspend fun saveProfile(profile: UserProfileEntity) {
        dao.saveUserProfile(profile)
    }
}
