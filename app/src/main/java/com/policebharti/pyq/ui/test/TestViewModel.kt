package com.policebharti.pyq.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policebharti.pyq.data.db.*
import com.policebharti.pyq.data.repository.BookmarkRepository
import com.policebharti.pyq.data.repository.QuestionRepository
import com.policebharti.pyq.util.TimerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * TestViewModel — manages test state, timer, answers, and scoring.
 *
 * MVVM: UI observes state flows, ViewModel talks to repositories.
 */
class TestViewModel(
    private val questionRepo: QuestionRepository,
    private val bookmarkRepo: BookmarkRepository
) : ViewModel() {

    // ── Session state ──
    private val _sessionId = MutableStateFlow<Long?>(null)
    val sessionId: StateFlow<Long?> = _sessionId.asStateFlow()

    private val _questions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val questions: StateFlow<List<QuestionEntity>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _answers = MutableStateFlow<List<TestAnswerEntity>>(emptyList())
    val answers: StateFlow<List<TestAnswerEntity>> = _answers.asStateFlow()

    private val _isTestCompleted = MutableStateFlow(false)
    val isTestCompleted: StateFlow<Boolean> = _isTestCompleted.asStateFlow()

    // ── Timer ──
    private var timerManager: TimerManager? = null
    val remainingMs: StateFlow<Long> get() = timerManager?.remainingMs ?: MutableStateFlow(0L)
    val isTimerRunning: StateFlow<Boolean> get() = timerManager?.isRunning ?: MutableStateFlow(false)

    /**
     * Start a new test session.
     */
    fun startTest(userId: Long, packId: Long, district: String, year: Int, setName: String) {
        viewModelScope.launch {
            val questions = questionRepo.getQuestionsByPack(packId)
            _questions.value = questions

            val sessionId = questionRepo.createSession(userId, packId, district, year, setName, questions.size)
            _sessionId.value = sessionId
            questionRepo.initAnswers(sessionId, questions)

            _answers.value = questionRepo.getAnswers(sessionId)

            // Start 90-min timer
            timerManager = TimerManager()
            timerManager?.start(viewModelScope)

            // Auto-submit when timer finishes
            viewModelScope.launch {
                timerManager?.isFinished?.collect { finished ->
                    if (finished) submitTest()
                }
            }
        }
    }

    /**
     * Resume a paused session.
     */
    fun resumeTest(sessionId: Long) {
        viewModelScope.launch {
            val session = questionRepo.getSession(sessionId) ?: return@launch
            _sessionId.value = sessionId
            _currentIndex.value = session.currentQuestionIdx

            val questions = questionRepo.getQuestionsByPack(session.packId)
            _questions.value = questions

            _answers.value = questionRepo.getAnswers(sessionId)

            timerManager = TimerManager(session.remainingTimeMs)
            timerManager?.start(viewModelScope)
        }
    }

    fun selectAnswer(questionId: Long, selectedOption: String) {
        viewModelScope.launch {
            val sid = _sessionId.value ?: return@launch
            val question = _questions.value.find { it.id == questionId } ?: return@launch
            questionRepo.submitAnswer(sid, questionId, selectedOption, question.correctOption)
            _answers.value = questionRepo.getAnswers(sid)
        }
    }

    fun navigateTo(index: Int) {
        _currentIndex.value = index
        viewModelScope.launch {
            val sid = _sessionId.value ?: return@launch
            val q = _questions.value.getOrNull(index) ?: return@launch
            questionRepo.markVisited(sid, q.id)
            _answers.value = questionRepo.getAnswers(sid)
        }
    }

    fun pauseTest() {
        timerManager?.pause()
        viewModelScope.launch {
            val sid = _sessionId.value ?: return@launch
            val remaining = timerManager?.remainingMs?.value ?: 0L
            questionRepo.pauseSession(sid, remaining, _currentIndex.value)
        }
    }

    fun submitTest() {
        timerManager?.stop()
        viewModelScope.launch {
            val sid = _sessionId.value ?: return@launch
            questionRepo.completeSession(sid)
            _isTestCompleted.value = true
        }
    }

    fun toggleBookmark(userId: Long, questionId: Long) {
        viewModelScope.launch {
            bookmarkRepo.toggleBookmark(userId, questionId)
        }
    }
}
