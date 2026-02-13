package com.policebharti.pyq.data.db

import androidx.room.*

// ──────────────────────────────────────────────
// Entity: User — local account information
// ──────────────────────────────────────────────
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "email")     val email: String,
    @ColumnInfo(name = "password")  val password: String,     // hashed
    @ColumnInfo(name = "dob")       val dob: String,          // yyyy-MM-dd
    @ColumnInfo(name = "district")  val district: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)

// ──────────────────────────────────────────────
// Entity: ContentPack — downloaded question pack
// ──────────────────────────────────────────────
@Entity(tableName = "content_packs")
data class ContentPackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "district")     val district: String,
    @ColumnInfo(name = "year")         val year: Int,
    @ColumnInfo(name = "set_name")     val setName: String,
    @ColumnInfo(name = "downloaded_at") val downloadedAt: Long = System.currentTimeMillis(),
    /** Pack auto-expires 3 days (72 h) after download */
    @ColumnInfo(name = "expires_at")   val expiresAt: Long = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000L,
    @ColumnInfo(name = "file_path")    val filePath: String = "",
    @ColumnInfo(name = "is_expired")   val isExpired: Boolean = false
)

// ──────────────────────────────────────────────
// Entity: Question — individual PYQ item
// ──────────────────────────────────────────────
@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = ContentPackEntity::class,
        parentColumns = ["id"],
        childColumns = ["pack_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["pack_id"])]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "pack_id")        val packId: Long,
    @ColumnInfo(name = "district")       val district: String,
    @ColumnInfo(name = "year")           val year: Int,
    @ColumnInfo(name = "set_name")       val setName: String,
    @ColumnInfo(name = "question_no")    val questionNo: Int,
    @ColumnInfo(name = "question_text")  val questionText: String,
    @ColumnInfo(name = "option_a")       val optionA: String,
    @ColumnInfo(name = "option_b")       val optionB: String,
    @ColumnInfo(name = "option_c")       val optionC: String,
    @ColumnInfo(name = "option_d")       val optionD: String,
    @ColumnInfo(name = "correct_option") val correctOption: String,   // "A","B","C","D"
    @ColumnInfo(name = "explanation")    val explanation: String = "",
    @ColumnInfo(name = "language")       val language: String = "mr", // mr = Marathi
    @ColumnInfo(name = "tags")           val tags: String = ""
)

// ──────────────────────────────────────────────
// Entity: TestSession — active / paused / completed test
// ──────────────────────────────────────────────
@Entity(tableName = "test_sessions")
data class TestSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id")              val userId: Long,
    @ColumnInfo(name = "pack_id")              val packId: Long,
    @ColumnInfo(name = "district")             val district: String,
    @ColumnInfo(name = "year")                 val year: Int,
    @ColumnInfo(name = "set_name")             val setName: String,
    /** "IN_PROGRESS", "PAUSED", "COMPLETED" */
    @ColumnInfo(name = "status")               val status: String = "IN_PROGRESS",
    @ColumnInfo(name = "current_question_idx") val currentQuestionIdx: Int = 0,
    /** Remaining time in milliseconds (90 min = 5_400_000) */
    @ColumnInfo(name = "remaining_time_ms")    val remainingTimeMs: Long = 90 * 60 * 1000L,
    @ColumnInfo(name = "total_questions")      val totalQuestions: Int = 0,
    @ColumnInfo(name = "correct_count")        val correctCount: Int = 0,
    @ColumnInfo(name = "wrong_count")          val wrongCount: Int = 0,
    @ColumnInfo(name = "unanswered_count")     val unansweredCount: Int = 0,
    @ColumnInfo(name = "score_percentage")     val scorePercentage: Float = 0f,
    @ColumnInfo(name = "started_at")           val startedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "completed_at")         val completedAt: Long? = null
)

// ──────────────────────────────────────────────
// Entity: TestAnswer — per-question answer state
// ──────────────────────────────────────────────
@Entity(
    tableName = "test_answers",
    foreignKeys = [
        ForeignKey(
            entity = TestSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["session_id"]),
        Index(value = ["question_id"])
    ]
)
data class TestAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "session_id")       val sessionId: Long,
    @ColumnInfo(name = "question_id")      val questionId: Long,
    /** User-selected option: "A","B","C","D" or "" if not answered */
    @ColumnInfo(name = "selected_option")  val selectedOption: String = "",
    @ColumnInfo(name = "is_correct")       val isCorrect: Boolean = false,
    /** "NOT_VISITED", "VISITED", "ANSWERED", "MARKED_FOR_REVIEW" */
    @ColumnInfo(name = "answer_status")    val answerStatus: String = "NOT_VISITED"
)

// ──────────────────────────────────────────────
// Entity: Bookmark — saved questions
// ──────────────────────────────────────────────
@Entity(
    tableName = "bookmarks",
    foreignKeys = [ForeignKey(
        entity = QuestionEntity::class,
        parentColumns = ["id"],
        childColumns = ["question_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["question_id"])]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id")      val userId: Long,
    @ColumnInfo(name = "question_id")  val questionId: Long,
    @ColumnInfo(name = "created_at")   val createdAt: Long = System.currentTimeMillis()
)

// ──────────────────────────────────────────────
// Entity: Vote — Yes/No with cloud sync flag
// ──────────────────────────────────────────────
@Entity(tableName = "votes")
data class VoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id")    val userId: Long,
    /** "YES" or "NO" */
    @ColumnInfo(name = "vote_type")  val voteType: String,
    @ColumnInfo(name = "is_synced")  val isSynced: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
