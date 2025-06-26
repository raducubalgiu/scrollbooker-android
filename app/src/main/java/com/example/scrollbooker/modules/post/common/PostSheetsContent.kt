package com.example.scrollbooker.modules.post.common

sealed class PostSheetsContent {
    data class ReviewsSheet(val userId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class CalendarSheet(val postId: Int): PostSheetsContent()
    object None: PostSheetsContent()
}