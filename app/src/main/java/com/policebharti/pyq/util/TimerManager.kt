package com.policebharti.pyq.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Timer Manager — 90-minute countdown with pause/resume.
 *
 * State is exposed as a StateFlow so Compose UI automatically recomposes.
 * Remaining time is in milliseconds and must be persisted to the DB
 * (via TestSession.remainingTimeMs) whenever the test is paused.
 *
 * Usage:
 *   val timer = TimerManager(initialRemainingMs = session.remainingTimeMs)
 *   timer.start(scope)
 *   timer.pause()
 *   timer.resume()
 *   timer.stop()
 */
class TimerManager(
    private val initialRemainingMs: Long = 90 * 60 * 1000L   // 90 minutes
) {
    private val _remainingMs = MutableStateFlow(initialRemainingMs)
    val remainingMs: StateFlow<Long> = _remainingMs.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private var timerJob: Job? = null

    /**
     * Start or resume the countdown in the given coroutine scope.
     */
    fun start(scope: CoroutineScope) {
        if (_isFinished.value) return
        _isRunning.value = true
        timerJob = scope.launch {
            while (_remainingMs.value > 0 && _isRunning.value) {
                delay(1000L)
                if (_isRunning.value) {
                    _remainingMs.value = (_remainingMs.value - 1000L).coerceAtLeast(0L)
                }
            }
            if (_remainingMs.value <= 0) {
                _isFinished.value = true
                _isRunning.value = false
            }
        }
    }

    /**
     * Pause the timer — remaining time is preserved.
     */
    fun pause() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    /**
     * Resume after pause.
     */
    fun resume(scope: CoroutineScope) {
        if (!_isFinished.value) {
            start(scope)
        }
    }

    /**
     * Stop the timer completely (test submitted).
     */
    fun stop() {
        _isRunning.value = false
        _isFinished.value = true
        timerJob?.cancel()
    }

    /**
     * Get the remaining time as a formatted string: "MM:SS"
     */
    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
