package com.policebharti.pyq.data.repository

import com.policebharti.pyq.data.db.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository: Questions & Content Packs
 * Handles pack downloads, question access, and expiry checks.
 */
class QuestionRepository(
    private val questionDao: QuestionDao,
    private val contentPackDao: ContentPackDao,
    private val testSessionDao: TestSessionDao,
    private val testAnswerDao: TestAnswerDao
) {
    // ── Content Packs ──

    fun getActivePacks(): Flow<List<ContentPackEntity>> =
        contentPackDao.getActivePacks()

    suspend fun downloadPack(district: String, year: Int, setName: String): Long {
        // In production, fetch from server. Here we create a local record.
        val pack = ContentPackEntity(
            district = district,
            year = year,
            setName = setName
        )
        return contentPackDao.insertPack(pack)
    }

    suspend fun getPack(district: String, year: Int, setName: String): ContentPackEntity? =
        contentPackDao.getPack(district, year, setName)

    suspend fun expireOldPacks() {
        contentPackDao.markExpiredPacks()
        contentPackDao.deleteExpiredPacks()
    }

    // ── Questions ──

    suspend fun getQuestionsByPack(packId: Long): List<QuestionEntity> =
        questionDao.getQuestionsByPack(packId)

    suspend fun getQuestionsBySelection(district: String, year: Int, setName: String): List<QuestionEntity> =
        questionDao.getQuestionsBySelection(district, year, setName)

    suspend fun insertQuestions(questions: List<QuestionEntity>) =
        questionDao.insertQuestions(questions)

    // ── Test Sessions ──

    suspend fun createSession(userId: Long, packId: Long, district: String, year: Int, setName: String, totalQuestions: Int): Long {
        val session = TestSessionEntity(
            userId = userId,
            packId = packId,
            district = district,
            year = year,
            setName = setName,
            totalQuestions = totalQuestions
        )
        return testSessionDao.insertSession(session)
    }

    suspend fun getSession(sessionId: Long): TestSessionEntity? =
        testSessionDao.getSessionById(sessionId)

    fun getPausedSessions(userId: Long): Flow<List<TestSessionEntity>> =
        testSessionDao.getPausedSessions(userId)

    fun getCompletedSessions(userId: Long): Flow<List<TestSessionEntity>> =
        testSessionDao.getCompletedSessions(userId)

    suspend fun pauseSession(sessionId: Long, remainingMs: Long, questionIdx: Int) {
        testSessionDao.updateSessionState(sessionId, "PAUSED", remainingMs, questionIdx)
    }

    suspend fun resumeSession(sessionId: Long, remainingMs: Long, questionIdx: Int) {
        testSessionDao.updateSessionState(sessionId, "IN_PROGRESS", remainingMs, questionIdx)
    }

    /**
     * Finalize the test: compute scores and mark session completed.
     */
    suspend fun completeSession(sessionId: Long) {
        val answers = testAnswerDao.getAnswersBySession(sessionId)
        val correct = answers.count { it.isCorrect }
        val answered = answers.count { it.answerStatus == "ANSWERED" }
        val wrong = answered - correct
        val unanswered = answers.size - answered
        val percent = if (answers.isNotEmpty()) (correct.toFloat() / answers.size) * 100f else 0f

        testSessionDao.completeSession(
            sessionId = sessionId,
            correct = correct,
            wrong = wrong,
            unanswered = unanswered,
            scorePercent = percent
        )
    }

    // ── Test Answers ──

    suspend fun initAnswers(sessionId: Long, questions: List<QuestionEntity>) {
        val answers = questions.map { q ->
            TestAnswerEntity(sessionId = sessionId, questionId = q.id)
        }
        testAnswerDao.insertAnswers(answers)
    }

    suspend fun getAnswers(sessionId: Long): List<TestAnswerEntity> =
        testAnswerDao.getAnswersBySession(sessionId)

    suspend fun submitAnswer(sessionId: Long, questionId: Long, selectedOption: String, correctOption: String) {
        val existing = testAnswerDao.getAnswer(sessionId, questionId) ?: return
        val updated = existing.copy(
            selectedOption = selectedOption,
            isCorrect = selectedOption.equals(correctOption, ignoreCase = true),
            answerStatus = "ANSWERED"
        )
        testAnswerDao.updateAnswer(updated)
    }

    suspend fun markVisited(sessionId: Long, questionId: Long) {
        val existing = testAnswerDao.getAnswer(sessionId, questionId) ?: return
        if (existing.answerStatus == "NOT_VISITED") {
            testAnswerDao.updateAnswer(existing.copy(answerStatus = "VISITED"))
        }
    }
}
