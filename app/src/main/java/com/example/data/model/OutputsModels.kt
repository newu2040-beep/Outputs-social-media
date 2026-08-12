package com.example.data.model

enum class PostType(val displayName: String) {
    STORY("Story"),
    OPINION("Opinion"),
    QUESTION("Question"),
    MYSTERY("Mystery"),
    REAL_INCIDENT("Real Incident"),
    INVESTIGATION("Investigation"),
    EXPERIENCE("Experience"),
    THEORY("Theory"),
    ANNOUNCEMENT("Announcement")
}

enum class PrivacyMode(val displayName: String, val description: String) {
    REAL_PROFILE("Real Profile", "Displays your public username, picture & bio"),
    PSEUDONYM("Pseudonym", "Displays a chosen pen name e.g. ShadowWriter"),
    ANONYMOUS_USERNAME("Anon Username", "Displays a generated anonymous username e.g. AnonymousFox"),
    ANONYMOUS_NUMBER("Anon Number", "Displays a numbered code e.g. Anonymous #4827"),
    COMPLETELY_ANONYMOUS("Completely Anonymous", "Displays only 'Anonymous' with no identifier")
}

enum class GenreTheme(val displayName: String) {
    DEFAULT("Default"),
    MYSTERY("Mystery"),
    HORROR("Horror"),
    ACTION("Action"),
    NOIR("Noir"),
    CYBER("Cyber"),
    ARCHIVE("Archive"),
    MINIMAL("Minimal"),
    DARK("Dark")
}

enum class MysteryStatus(val displayName: String) {
    OPEN("Open"),
    INVESTIGATING("Investigating"),
    STRONG_THEORY("Strong Theory"),
    SOLVED("Solved"),
    DEBUNKED("Debunked"),
    UNRESOLVED("Unresolved")
}

enum class EvidenceType(val displayName: String) {
    PHOTOGRAPH("Photograph"),
    VIDEO("Video"),
    AUDIO("Audio Note"),
    DOCUMENT("Document"),
    SCREENSHOT("Screenshot"),
    TIMELINE_EVENT("Timeline Event"),
    TESTIMONY("Testimony")
}

data class TimelineEventItem(
    val id: String,
    val timeLabel: String,
    val title: String,
    val description: String,
    val isVerified: Boolean = false
)

data class EvidenceItem(
    val id: String,
    val postId: String,
    val type: EvidenceType,
    val title: String,
    val description: String,
    val source: String,
    val contributor: String,
    val confidenceLevel: String = "Medium", // High, Medium, Low, Under Review
    val mediaUrl: String? = null,
    val dateAdded: String = "Just now"
)

data class TheoryItem(
    val id: String,
    val postId: String,
    val authorIdentity: String,
    val authorMode: PrivacyMode,
    val title: String,
    val description: String,
    val votesCount: Int = 0,
    val status: String = "Under Review", // Supported, Debunked, Accepted Solution, Under Review
    val isUpvoted: Boolean = false
)

data class UserVerificationState(
    val dobDay: Int = 1,
    val dobMonth: Int = 1,
    val dobYear: Int = 1998,
    val citizenshipCountry: String = "United States",
    val isAgeVerified: Boolean = true,
    val isIdentityVerified: Boolean = true,
    val publicUsername: String = "ShadowWriter",
    val publicBio: String = "Investigating urban folklore & digital anomalies.",
    val privacyMode: PrivacyMode = PrivacyMode.ANONYMOUS_NUMBER,
    val anonymousNumberCode: Int = 4827,
    val rotateIdentityPerPost: Boolean = false
)
