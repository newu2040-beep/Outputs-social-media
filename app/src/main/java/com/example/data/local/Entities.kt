package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.EvidenceType
import com.example.data.model.GenreTheme
import com.example.data.model.MysteryStatus
import com.example.data.model.PostType
import com.example.data.model.PrivacyMode

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val postType: PostType,
    val genreTheme: GenreTheme = GenreTheme.DEFAULT,
    val mysteryStatus: MysteryStatus = MysteryStatus.OPEN,
    val privacyMode: PrivacyMode = PrivacyMode.ANONYMOUS_NUMBER,
    val authorDisplayName: String,
    val authorAvatar: String? = null,
    val timestamp: String,
    val category: String,
    val views: Int = 120,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val savesCount: Int = 0,
    val isSaved: Boolean = false,
    val isLiked: Boolean = false,
    val isAudio: Boolean = false,
    val audioUrl: String? = null,
    val audioDuration: String? = null,
    val mediaUrlsJson: String = "[]",
    val locationName: String? = null,
    val knownFactsJson: String = "[]",
    val unknownFactsJson: String = "[]",
    val timelineEventsJson: String = "[]",
    val acceptedAnswerId: String? = null
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val parentCommentId: String? = null,
    val authorDisplayName: String,
    val privacyMode: PrivacyMode = PrivacyMode.ANONYMOUS_NUMBER,
    val content: String,
    val timestamp: String,
    val likesCount: Int = 0,
    val voiceUrl: String? = null,
    val isCreatorReply: Boolean = false
)

@Entity(tableName = "evidence")
data class EvidenceEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val type: EvidenceType,
    val title: String,
    val description: String,
    val source: String,
    val contributor: String,
    val confidenceLevel: String = "Medium",
    val mediaUrl: String? = null,
    val dateAdded: String = "Today"
)

@Entity(tableName = "theories")
data class TheoryEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val authorIdentity: String,
    val authorMode: PrivacyMode,
    val title: String,
    val description: String,
    val votesCount: Int = 0,
    val status: String = "Under Review",
    val isUpvoted: Boolean = false
)

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val isPrivate: Boolean = false,
    val memberCount: Int = 1,
    val myMemberIdentity: String = "Member #1",
    val category: String = "General",
    val bannerColorHex: String = "#8A2BE2",
    val isJoined: Boolean = false
)

@Entity(tableName = "group_messages")
data class GroupMessageEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val senderIdentity: String,
    val content: String,
    val timestamp: String,
    val mediaUrl: String? = null,
    val isVoice: Boolean = false
)

@Entity(tableName = "direct_messages")
data class DirectMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderId: String,
    val senderDisplayName: String,
    val senderAvatar: String? = null,
    val content: String,
    val timestamp: String,
    val isRequest: Boolean = false,
    val isVoice: Boolean = false,
    val mediaUrl: String? = null
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val dobDay: Int = 14,
    val dobMonth: Int = 5,
    val dobYear: Int = 1999,
    val citizenshipCountry: String = "United States",
    val isAgeVerified: Boolean = true,
    val isIdentityVerified: Boolean = true,
    val publicUsername: String = "ShadowWriter",
    val publicBio: String = "Investigating urban folklore, night anomalies, and digital ghost stories.",
    val privacyMode: PrivacyMode = PrivacyMode.ANONYMOUS_NUMBER,
    val anonymousNumberCode: Int = 7294,
    val rotateIdentityPerPost: Boolean = true,
    val selectedGlobalTheme: String = "Dark"
)
