package com.policebharti.pyq.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database — single source of truth for all local data.
 * Uses singleton pattern for efficiency on low-end devices.
 */
@Database(
    entities = [
        UserEntity::class,
        ContentPackEntity::class,
        QuestionEntity::class,
        TestSessionEntity::class,
        TestAnswerEntity::class,
        BookmarkEntity::class,
        VoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun contentPackDao(): ContentPackDao
    abstract fun questionDao(): QuestionDao
    abstract fun testSessionDao(): TestSessionDao
    abstract fun testAnswerDao(): TestAnswerDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun voteDao(): VoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "police_bharti_pyq.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
