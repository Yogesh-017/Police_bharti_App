package com.policebharti.pyq.data.remote

import retrofit2.http.*

/**
 * Retrofit API service — future cloud sync endpoints.
 * These are stubs that define the contract for when the backend is built.
 *
 * Base URL will be configured in a DI module or companion object.
 */
interface ApiService {

    // ── Pack Manifest ──
    /** GET the list of available content packs from the server */
    @GET("api/v1/packs")
    suspend fun getPackManifest(): List<PackManifestItem>

    /** Download a specific pack's question data */
    @GET("api/v1/packs/{packId}/questions")
    suspend fun getPackQuestions(@Path("packId") packId: String): List<QuestionJson>

    // ── Vote Sync ──
    /** Submit a single vote to the cloud */
    @POST("api/v1/votes")
    suspend fun submitVote(@Body body: Map<String, String>): VoteResponse

    /** Get aggregate vote counts */
    @GET("api/v1/votes/counts")
    suspend fun getVoteCounts(): VoteCounts
}

// ── Response models ──

data class PackManifestItem(
    val packId: String,
    val district: String,
    val year: Int,
    val setName: String,
    val questionCount: Int,
    val version: Int
)

data class QuestionJson(
    val id: String,
    val questionNo: Int,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String,
    val explanation: String = "",
    val language: String = "mr",
    val tags: String = ""
)

data class VoteResponse(
    val success: Boolean,
    val message: String
)

data class VoteCounts(
    val yesCount: Int,
    val noCount: Int,
    val totalCount: Int
)
