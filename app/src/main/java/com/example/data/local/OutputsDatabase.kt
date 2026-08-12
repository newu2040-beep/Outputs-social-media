package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        EvidenceEntity::class,
        TheoryEntity::class,
        GroupEntity::class,
        GroupMessageEntity::class,
        DirectMessageEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OutputsDatabase : RoomDatabase() {

    abstract fun outputsDao(): OutputsDao

    companion object {
        @Volatile
        private var INSTANCE: OutputsDatabase? = null

        fun getDatabase(context: Context): OutputsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OutputsDatabase::class.java,
                    "outputs_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
