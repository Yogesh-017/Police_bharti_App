package com.policebharti.pyq.data.repository

import com.policebharti.pyq.data.db.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository: Bookmarks
 * Full CRUD for saving/removing bookmarked questions.
 */
class BookmarkRepository(
    private val bookmarkDao: BookmarkDao
) {
    suspend fun toggleBookmark(userId: Long, questionId: Long) {
        if (bookmarkDao.isBookmarked(userId, questionId)) {
            bookmarkDao.removeBookmark(userId, questionId)
        } else {
            bookmarkDao.insertBookmark(
                BookmarkEntity(userId = userId, questionId = questionId)
            )
        }
    }

    suspend fun isBookmarked(userId: Long, questionId: Long): Boolean =
        bookmarkDao.isBookmarked(userId, questionId)

    fun getBookmarkedQuestions(userId: Long): Flow<List<QuestionEntity>> =
        bookmarkDao.getBookmarkedQuestions(userId)

    fun getBookmarks(userId: Long): Flow<List<BookmarkEntity>> =
        bookmarkDao.getBookmarks(userId)
}
