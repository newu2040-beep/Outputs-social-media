package com.example.data.local

import com.example.data.model.EvidenceType
import com.example.data.model.GenreTheme
import com.example.data.model.MysteryStatus
import com.example.data.model.PostType
import com.example.data.model.PrivacyMode

object SeedData {

    val initialPosts = listOf(
        PostEntity(
            id = "post_101",
            title = "The Midnight Signal of Echo Valley",
            content = """
                Every night at exactly 03:14 AM, a low-frequency radio broadcast cuts through static across Echo Valley. 
                It plays 12 notes on an organ followed by an encrypted sequence of numbers. 

                No cell towers or active stations are registered within 30 miles of the abandoned observatory where the signal originates. 
                Last Thursday, three independent shortwave radio enthusiasts recorded the signal simultaneously from different locations.

                What makes this stranger is that the number sequence changes slightly every full moon, but the organ motif remains identical to a 1928 gramophone cylinder that was reportedly lost in a fire.
            """.trimIndent(),
            postType = PostType.MYSTERY,
            genreTheme = GenreTheme.MYSTERY,
            mysteryStatus = MysteryStatus.INVESTIGATING,
            privacyMode = PrivacyMode.ANONYMOUS_NUMBER,
            authorDisplayName = "Anonymous #8192",
            timestamp = "2 hours ago",
            category = "Unexplained",
            views = 1420,
            likesCount = 384,
            commentsCount = 67,
            savesCount = 112,
            isSaved = true,
            isLiked = true,
            isAudio = true,
            audioUrl = "sample_audio_1.mp3",
            audioDuration = "03:14",
            knownFactsJson = """["Signal originates near 38.8951° N, 77.0364° W", "Frequencies match 462.550 MHz", "Recorded by 3 separate ham radio operators on Aug 10"]""",
            unknownFactsJson = """["Source power generator", "Identity of organist in the 1928 recording", "Meaning of the lunar sequence variation"]""",
            timelineEventsJson = """[{"id":"t1","timeLabel":"03:14:00 AM","title":"Organ Prelude","description":"12-tone organ sequence commences"},{"id":"t2","timeLabel":"03:15:10 AM","title":"Numeric Transmission","description":"Voice reads sequence: 4-18-99-23-01"},{"id":"t3","timeLabel":"03:16:30 AM","title":"Abrupt Static Cut","description":"Signal terminates into background thermal noise"}]"""
        ),
        PostEntity(
            id = "post_102",
            title = "Real Incident: Unclaimed Freight Box #772 at Station B",
            content = """
                A sealed wooden cargo crate arrived at the Northern Freight Hub on July 19th with no sender address and an invalid receiver tax ID. 

                When customs scanned the box using thermal imaging, they detected a localized temperature anomaly: the inside of the box remains precisely 4.2°C colder than ambient room temperature without any electrical power source, battery, or chemical refrigerant trace.

                Port security opened the outer casing under supervision and found a heavy iron cylinder sealed with copper rivets and brass stenciling reading 'PROJECT ARCHIVE 1954'.
            """.trimIndent(),
            postType = PostType.REAL_INCIDENT,
            genreTheme = GenreTheme.ARCHIVE,
            mysteryStatus = MysteryStatus.OPEN,
            privacyMode = PrivacyMode.PSEUDONYM,
            authorDisplayName = "StationMaster_X",
            timestamp = "5 hours ago",
            category = "Real Incident",
            views = 2890,
            likesCount = 512,
            commentsCount = 89,
            savesCount = 201,
            isSaved = false,
            isLiked = false,
            locationName = "Northern Freight Logistics Hub - Gate B",
            knownFactsJson = """["Box weight: 142 kg", "Internal temperature constant at 4.2°C", "Crate origin tagged as Port Bremerhaven"]""",
            unknownFactsJson = """["Who paid the shipping manifest", "Contents inside the iron cylinder"]""",
            timelineEventsJson = """[{"id":"t1","timeLabel":"July 19, 08:30 AM","title":"Crate Offloaded","description":"Manifest flag raised for missing tax ID"},{"id":"t2","timeLabel":"July 21, 02:00 PM","title":"Thermal Scan","description":"Cold anomaly detected by customs inspectors"}]"""
        ),
        PostEntity(
            id = "post_103",
            title = "Voice Story: The Whisper in the Recording Studio",
            content = """
                Listen to this 45-second audio clip recorded during a solo vocal session last midnight. 
                I was completely alone in a soundproof booth with active noise cancellation. 
                At second 18, between two verse takes, a distinct second voice murmurs 'Look behind the soundboard.' 

                I checked the multitrack meters and the voice was isolated exclusively on Microphone #3, which was unplugged at the patch bay.
            """.trimIndent(),
            postType = PostType.STORY,
            genreTheme = GenreTheme.HORROR,
            mysteryStatus = MysteryStatus.OPEN,
            privacyMode = PrivacyMode.ANONYMOUS_USERNAME,
            authorDisplayName = "SoundGhost",
            timestamp = "Yesterday",
            category = "Voice Stories",
            views = 3100,
            likesCount = 740,
            commentsCount = 142,
            savesCount = 310,
            isAudio = true,
            audioUrl = "whisper_studio.mp3",
            audioDuration = "00:45",
            knownFactsJson = """["Isolated to track 3", "Patch bay mic was disconnected", "Studio walls treated with 4-inch acoustic foam"]""",
            unknownFactsJson = """["Origin of the acoustic energy", "What is stored behind the main soundboard rack"]"""
        ),
        PostEntity(
            id = "post_104",
            title = "Opinion: Why Digital Anonymity is Essential for Honest Civic Dialogue",
            content = """
                When public identity is tied to every opinion, discourse devolves into social posturing and fear of reprisal. 
                True storytelling and whistleblowing require a sanctuary where ideas and evidence stand purely on their intrinsic value rather than author status or clout.

                Privacy-first platforms like Outputs allow adults to share uncomfortable truths, unsolved family mysteries, and genuine personal struggles without fearing permanent digital footprint consequences.
            """.trimIndent(),
            postType = PostType.OPINION,
            genreTheme = GenreTheme.MINIMAL,
            mysteryStatus = MysteryStatus.OPEN,
            privacyMode = PrivacyMode.REAL_PROFILE,
            authorDisplayName = "Marcus Vance",
            timestamp = "2 days ago",
            category = "Civic & Privacy",
            views = 980,
            likesCount = 210,
            commentsCount = 34,
            savesCount = 56
        ),
        PostEntity(
            id = "post_105",
            title = "SOLVED: The Vanishing Lighthouse Clockwork of 1974",
            content = """
                CASE CLOSED: For 50 years, historians debated how the brass rotation gears of the St. Jude Point lighthouse vanished overnight in November 1974 without a single lock being broken.

                Thanks to evidence submitted by community member @Archivist_99 (1971 maintenance logbook recovered from an estate sale), we confirmed the lighthouse keeper transferred the clockwork mechanism to a navy shipyard for emergency repair during a storm surge. The official maritime ledger was misfiled under 'Lighthouse Supply B'.
            """.trimIndent(),
            postType = PostType.MYSTERY,
            genreTheme = GenreTheme.NOIR,
            mysteryStatus = MysteryStatus.SOLVED,
            privacyMode = PrivacyMode.PSEUDONYM,
            authorDisplayName = "Maritime_Detective",
            timestamp = "3 days ago",
            category = "Historical Mysteries",
            views = 4200,
            likesCount = 1120,
            commentsCount = 189,
            savesCount = 450,
            acceptedAnswerId = "theory_301"
        )
    )

    val initialComments = listOf(
        CommentEntity(
            id = "c_1",
            postId = "post_101",
            authorDisplayName = "Anonymous #3301",
            privacyMode = PrivacyMode.ANONYMOUS_NUMBER,
            content = "I ran the audio through a spectrogram analyzer. The organ harmonics contain a hidden watermark shaped like a compass rose at 14 kHz!",
            timestamp = "1 hour ago",
            likesCount = 42
        ),
        CommentEntity(
            id = "c_2",
            postId = "post_101",
            authorDisplayName = "StationMaster_X",
            privacyMode = PrivacyMode.PSEUDONYM,
            content = "Could this be linked to the 1950s Civil Defense warning mesh in the tri-state area?",
            timestamp = "45 mins ago",
            likesCount = 18
        ),
        CommentEntity(
            id = "c_3",
            postId = "post_102",
            authorDisplayName = "CargoInspector",
            privacyMode = PrivacyMode.ANONYMOUS_USERNAME,
            content = "4.2°C is very close to liquid helium insulation boil-off residual temp. Check the inner vacuum chamber wall!",
            timestamp = "3 hours ago",
            likesCount = 29
        )
    )

    val initialEvidence = listOf(
        EvidenceEntity(
            id = "e_1",
            postId = "post_101",
            type = EvidenceType.AUDIO,
            title = "Shortwave Recording Waveform (462.55 MHz)",
            description = "High resolution audio recording captured with RTLSDR receiver in Echo Valley.",
            source = "Ham Radio Operator #14",
            contributor = "Anonymous #8192",
            confidenceLevel = "High",
            dateAdded = "Aug 11"
        ),
        EvidenceEntity(
            id = "e_2",
            postId = "post_101",
            type = EvidenceType.DOCUMENT,
            title = "1928 Gramophone Cylinder Catalog Index",
            description = "Archival catalog sheet matching the 12-tone organ sequence score.",
            source = "County Historical Society Archive",
            contributor = "Archivist_99",
            confidenceLevel = "High",
            dateAdded = "Aug 10"
        ),
        EvidenceEntity(
            id = "e_3",
            postId = "post_102",
            type = EvidenceType.PHOTOGRAPH,
            title = "Thermal Scanner Image of Crate #772",
            description = "Shows distinct cold blue core inside wooden crate casing.",
            source = "Station B Port Customs Scan Log",
            contributor = "StationMaster_X",
            confidenceLevel = "High",
            dateAdded = "July 21"
        )
    )

    val initialTheories = listOf(
        TheoryEntity(
            id = "theory_101",
            postId = "post_101",
            authorIdentity = "AnonInvestigator #99",
            authorMode = PrivacyMode.ANONYMOUS_NUMBER,
            title = "Automated Seismic Telemetry Beacon",
            description = "The signal might be an old automated geological telemetry unit designed in the 1960s to monitor subterranean rock shifts using radio frequency bursts.",
            votesCount = 78,
            status = "Supported",
            isUpvoted = true
        ),
        TheoryEntity(
            id = "theory_102",
            postId = "post_101",
            authorIdentity = "CyberCryptographer",
            authorMode = PrivacyMode.ANONYMOUS_USERNAME,
            title = "Numbers Station / One-Time Pad",
            description = "The number sequences match classical cryptographic one-time pad distributions. The lunar timing aligns with satellite orbital passes.",
            votesCount = 45,
            status = "Under Review"
        ),
        TheoryEntity(
            id = "theory_301",
            postId = "post_105",
            authorIdentity = "Archivist_99",
            authorMode = PrivacyMode.PSEUDONYM,
            title = "Misfiled Navy Maintenance Transfer",
            description = "Logbook entry #714 shows naval engineers temporarily dismantled the rotation gears to reinforce them against hurricane storm surges.",
            votesCount = 142,
            status = "Accepted Solution"
        )
    )

    val initialGroups = listOf(
        GroupEntity(
            id = "g_101",
            name = "Midnight Investigators",
            description = "A collective dedicated to decoding radio signals, cold cases, and night anomalies.",
            isPrivate = false,
            memberCount = 1420,
            myMemberIdentity = "Member #4827",
            category = "Mysteries",
            bannerColorHex = "#7C4DFF",
            isJoined = true
        ),
        GroupEntity(
            id = "g_102",
            name = "Kathmandu Mystery Group",
            description = "Investigating local folklore, mountain tales, and historic artifact discoveries.",
            isPrivate = false,
            memberCount = 890,
            myMemberIdentity = "Member #109",
            category = "Regional",
            bannerColorHex = "#FF5252",
            isJoined = false
        ),
        GroupEntity(
            id = "g_103",
            name = "Urban Cryptids & Lost Media",
            description = "Uncovering lost broadcasts, forgotten films, and urban myths.",
            isPrivate = false,
            memberCount = 2300,
            myMemberIdentity = "Member #88",
            category = "Media & Lore",
            bannerColorHex = "#00E676",
            isJoined = true
        )
    )

    val initialGroupMessages = listOf(
        GroupMessageEntity(
            id = "gm_1",
            groupId = "g_101",
            senderIdentity = "Member #310",
            content = "Has anyone listened to the latest Echo Valley transmission? The 3rd frequency shifted slightly.",
            timestamp = "10:14 AM"
        ),
        GroupMessageEntity(
            id = "gm_2",
            groupId = "g_101",
            senderIdentity = "Member #4827",
            content = "Yes! I uploaded the SDR spectrogram to the case file evidence board.",
            timestamp = "10:16 AM"
        )
    )

    val initialDirectMessages = listOf(
        DirectMessageEntity(
            id = "dm_1",
            conversationId = "convo_1",
            senderId = "user_anon_9",
            senderDisplayName = "CipherResearcher",
            senderAvatar = null,
            content = "Hey! Saw your theory on Post #101. Do you have the raw WAV file from Echo Valley?",
            timestamp = "Yesterday 09:30 PM",
            isRequest = false
        ),
        DirectMessageEntity(
            id = "dm_2",
            conversationId = "convo_1",
            senderId = "self",
            senderDisplayName = "Me (Anon #7294)",
            senderAvatar = null,
            content = "Sure! I can share the drive link or audio sample.",
            timestamp = "Yesterday 09:35 PM",
            isRequest = false
        ),
        DirectMessageEntity(
            id = "dm_3",
            conversationId = "convo_req_1",
            senderId = "user_unknown",
            senderDisplayName = "MysteriousSender_00",
            senderAvatar = null,
            content = "I have additional photos of Crate #772 at Freight Hub B.",
            timestamp = "2 hours ago",
            isRequest = true
        )
    )

    val initialProfile = UserProfileEntity(
        id = 1,
        dobDay = 14,
        dobMonth = 5,
        dobYear = 1999,
        citizenshipCountry = "United States",
        isAgeVerified = true,
        isIdentityVerified = true,
        publicUsername = "ShadowWriter",
        publicBio = "Investigating urban folklore, night radio anomalies, and digital ghost stories.",
        privacyMode = PrivacyMode.ANONYMOUS_NUMBER,
        anonymousNumberCode = 7294,
        rotateIdentityPerPost = true,
        selectedGlobalTheme = "Dark"
    )
}
