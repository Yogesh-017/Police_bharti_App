package com.policebharti.pyq.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ──────────────────────────────────────────────
// DAO: User
// ──────────────────────────────────────────────
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?
}

// ──────────────────────────────────────────────
// DAO: ContentPack
// ──────────────────────────────────────────────
@Dao
interface ContentPackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPack(pack: ContentPackEntity): Long

    @Query("SELECT * FROM content_packs WHERE is_expired = 0 ORDER BY downloaded_at DESC")
    fun getActivePacks(): Flow<List<ContentPackEntity>>

    @Query("SELECT * FROM content_packs WHERE district = :district AND year = :year AND set_name = :setName AND is_expired = 0 LIMIT 1")
    suspend fun getPack(district: String, year: Int, setName: String): ContentPackEntity?

    @Query("UPDATE content_packs SET is_expired = 1 WHERE expires_at < :now")
    suspend fun markExpiredPacks(now: Long = System.currentTimeMillis())

    @Query("DELETE FROM content_packs WHERE is_expired = 1")
    suspend fun deleteExpiredPacks()

    @Query("SELECT * FROM content_packs WHERE expires_at < :now AND is_expired = 0")
    suspend fun getPacksToExpire(now: Long = System.currentTimeMillis()): List<ContentPackEntity>
}

// ──────────────────────────────────────────────
// DAO: Question
// ──────────────────────────────────────────────
@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("SELECT * FROM questions WHERE pack_id = :packId ORDER BY question_no ASC")
    suspend fun getQuestionsByPack(packId: Long): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionById(id: Long): QuestionEntity?

    @Query("SELECT COUNT(*) FROM questions WHERE pack_id = :packId")
    suspend fun getQuestionCount(packId: Long): Int

    @Query("SELECT * FROM questions WHERE district = :district AND year = :year AND set_name = :setName ORDER BY question_no ASC")
    suspend fun getQuestionsBySelection(district: String, year: Int, setName: String): List<QuestionEntity>
}

// ──────────────────────────────────────────────
// DAO: TestSession
// ──────────────────────────────────────────────
@Dao
interface TestSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TestSessionEntity): Long

    @Update
    suspend fun updateSession(session: TestSessionEntity)

    @Query("SELECT * FROM test_sessions WHERE user_id = :userId AND status = 'PAUSED' ORDER BY started_at DESC")
    fun getPausedSessions(userId: Long): Flow<List<TestSessionEntity>>

    @Query("SELECT * FROM test_sessions WHERE id = :id LIMIT 1")
    suspend fun getSessionById(id: Long): TestSessionEntity?

    @Query("SELECT * FROM test_sessions WHERE user_id = :userId AND status = 'COMPLETED' ORDER BY completed_at DESC")
    fun getCompletedSessions(userId: Long): Flow<List<TestSessionEntity>>

    @Query("UPDATE test_sessions SET status = :status, remaining_time_ms = :remainingMs, current_question_idx = :questionIdx WHERE id = :sessionId")
    suspend fun updateSessionState(sessionId: Long, status: String, remainingMs: Long, questionIdx: Int)

    @Query("UPDATE test_sessions SET status = 'COMPLETED', completed_at = :completedAt, correct_count = :correct, wrong_count = :wrong, unanswered_count = :unanswered, score_percentage = :scorePercent WHERE id = :sessionId")
    suspend fun completeSession(
        sessionId: Long,
        completedAt: Long = System.currentTimeMillis(),
        correct: Int,
        wrong: Int,
        unanswered: Int,
        scorePercent: Float
    )
}

// ──────────────────────────────────────────────
// DAO: TestAnswer
// ──────────────────────────────────────────────
@Dao
interface TestAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<TestAnswerEntity>)

    @Update
    suspend fun updateAnswer(answer: TestAnswerEntity)

    @Query("SELECT * FROM test_answers WHERE session_id = :sessionId ORDER BY question_id ASC")
    suspend fun getAnswersBySession(sessionId: Long): List<TestAnswerEntity>

    @Query("SELECT * FROM test_answers WHERE session_id = :sessionId AND question_id = :questionId LIMIT 1")
    suspend fun getAnswer(sessionId: Long, questionId: Long): TestAnswerEntity?

    @Query("SELECT COUNT(*) FROM test_answers WHERE session_id = :sessionId AND answer_status = 'ANSWERED'")
    suspend fun getAnsweredCount(sessionId: Long): Int

    @Query("SELECT COUNT(*) FROM test_answers WHERE session_id = :sessionId AND is_correct = 1")
    suspend fun getCorrectCount(sessionId: Long): Int
}

// ──────────────────────────────────────────────
// DAO: Bookmark
// ──────────────────────────────────────────────
@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE user_id = :userId AND question_id = :questionId")
    suspend fun removeBookmark(userId: Long, questionId: Long)

    @Query("SELECT * FROM bookmarks WHERE user_id = :userId ORDER BY created_at DESC")
    fun getBookmarks(userId: Long): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE user_id = :userId AND question_id = :questionId)")
    suspend fun isBookmarked(userId: Long, questionId: Long): Boolean

    /** Join with questions to get full question data for bookmark screen */
    @Query("""
        SELECT q.* FROM questions q 
        INNER JOIN bookmarks b ON b.question_id = q.id 
        WHERE b.user_id = :userId 
        ORDER BY b.created_at DESC
    """)
    fun getBookmarkedQuestions(userId: Long): Flow<List<QuestionEntity>>
}

// ──────────────────────────────────────────────
// DAO: Vote
// ──────────────────────────────────────────────
@Dao
interface VoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVote(vote: VoteEntity): Long

    @Query("SELECT * FROM votes WHERE is_synced = 0")
    suspend fun getUnsyncedVotes(): List<VoteEntity>

    @Query("UPDATE votes SET is_synced = 1 WHERE id = :voteId")
    suspend fun markSynced(voteId: Long)

    @Query("SELECT * FROM votes WHERE user_id = :userId ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestVote(userId: Long): VoteEntity?
}
