package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OutputsDao {

    // Posts
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: String): Flow<PostEntity?>

    @Query("SELECT * FROM posts WHERE isSaved = 1 ORDER BY id DESC")
    fun getSavedPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE mysteryStatus = 'SOLVED' OR mysteryStatus = 'DEBUNKED' ORDER BY id DESC")
    fun getSolvedPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("UPDATE posts SET isSaved = CASE WHEN isSaved = 1 THEN 0 ELSE 1 END WHERE id = :postId")
    suspend fun toggleSavePost(postId: String)

    @Query("UPDATE posts SET isLiked = CASE WHEN isLiked = 1 THEN 0 ELSE 1 END, likesCount = likesCount + (CASE WHEN isLiked = 1 THEN -1 ELSE 1 END) WHERE id = :postId")
    suspend fun toggleLikePost(postId: String)

    @Query("UPDATE posts SET mysteryStatus = :newStatus WHERE id = :postId")
    suspend fun updateMysteryStatus(postId: String, newStatus: String)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: String)

    // Comments
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY id ASC")
    fun getCommentsForPost(postId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    // Evidence
    @Query("SELECT * FROM evidence WHERE postId = :postId ORDER BY id ASC")
    fun getEvidenceForPost(postId: String): Flow<List<EvidenceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: EvidenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidenceList(evidenceList: List<EvidenceEntity>)

    // Theories
    @Query("SELECT * FROM theories WHERE postId = :postId ORDER BY votesCount DESC")
    fun getTheoriesForPost(postId: String): Flow<List<TheoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheory(theory: TheoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheories(theories: List<TheoryEntity>)

    @Query("UPDATE theories SET isUpvoted = CASE WHEN isUpvoted = 1 THEN 0 ELSE 1 END, votesCount = votesCount + (CASE WHEN isUpvoted = 1 THEN -1 ELSE 1 END) WHERE id = :theoryId")
    suspend fun toggleTheoryUpvote(theoryId: String)

    @Query("UPDATE theories SET status = :status WHERE id = :theoryId")
    suspend fun updateTheoryStatus(theoryId: String, status: String)

    // Groups
    @Query("SELECT * FROM groups ORDER BY id ASC")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Query("UPDATE groups SET isJoined = CASE WHEN isJoined = 1 THEN 0 ELSE 1 END, memberCount = memberCount + (CASE WHEN isJoined = 1 THEN -1 ELSE 1 END) WHERE id = :groupId")
    suspend fun toggleJoinGroup(groupId: String)

    // Group Messages
    @Query("SELECT * FROM group_messages WHERE groupId = :groupId ORDER BY id ASC")
    fun getGroupMessages(groupId: String): Flow<List<GroupMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupMessage(message: GroupMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupMessages(messages: List<GroupMessageEntity>)

    // Direct Messages
    @Query("SELECT * FROM direct_messages ORDER BY id ASC")
    fun getAllDirectMessages(): Flow<List<DirectMessageEntity>>

    @Query("SELECT * FROM direct_messages WHERE conversationId = :convoId ORDER BY id ASC")
    fun getMessagesForConversation(convoId: String): Flow<List<DirectMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirectMessage(message: DirectMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirectMessages(messages: List<DirectMessageEntity>)

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)
}
