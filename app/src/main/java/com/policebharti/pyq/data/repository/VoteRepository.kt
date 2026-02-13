package com.policebharti.pyq.data.repository

import com.policebharti.pyq.data.db.*
import com.policebharti.pyq.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

/**
 * Repository: Votes
 * Handles local vote CRUD and cloud sync when online.
 */
class VoteRepository(
    private val voteDao: VoteDao,
    private val apiService: ApiService? = null   // null = offline mode
) {
    suspend fun castVote(userId: Long, voteType: String): Long {
        val vote = VoteEntity(userId = userId, voteType = voteType)
        return voteDao.insertVote(vote)
    }

    suspend fun getLatestVote(userId: Long): VoteEntity? =
        voteDao.getLatestVote(userId)

    /**
     * Sync unsynced votes to cloud.
     * Called opportunistically when network is available.
     */
    suspend fun syncVotes() {
        val api = apiService ?: return
        val unsynced = voteDao.getUnsyncedVotes()
        for (vote in unsynced) {
            try {
                api.submitVote(
                    mapOf(
                        "user_id" to vote.userId.toString(),
                        "vote_type" to vote.voteType
                    )
                )
                voteDao.markSynced(vote.id)
            } catch (_: Exception) {
                // Retry on next sync cycle
            }
        }
    }
}
