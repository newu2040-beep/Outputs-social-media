package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.EvidenceType
import com.example.data.model.GenreTheme
import com.example.data.model.MysteryStatus
import com.example.data.model.PostType
import com.example.data.model.PrivacyMode

class Converters {
    @TypeConverter
    fun fromPostType(value: PostType): String = value.name

    @TypeConverter
    fun toPostType(value: String): PostType = runCatching { PostType.valueOf(value) }.getOrDefault(PostType.STORY)

    @TypeConverter
    fun fromGenreTheme(value: GenreTheme): String = value.name

    @TypeConverter
    fun toGenreTheme(value: String): GenreTheme = runCatching { GenreTheme.valueOf(value) }.getOrDefault(GenreTheme.DEFAULT)

    @TypeConverter
    fun fromMysteryStatus(value: MysteryStatus): String = value.name

    @TypeConverter
    fun toMysteryStatus(value: String): MysteryStatus = runCatching { MysteryStatus.valueOf(value) }.getOrDefault(MysteryStatus.OPEN)

    @TypeConverter
    fun fromPrivacyMode(value: PrivacyMode): String = value.name

    @TypeConverter
    fun toPrivacyMode(value: String): PrivacyMode = runCatching { PrivacyMode.valueOf(value) }.getOrDefault(PrivacyMode.ANONYMOUS_NUMBER)

    @TypeConverter
    fun fromEvidenceType(value: EvidenceType): String = value.name

    @TypeConverter
    fun toEvidenceType(value: String): EvidenceType = runCatching { EvidenceType.valueOf(value) }.getOrDefault(EvidenceType.PHOTOGRAPH)
}
