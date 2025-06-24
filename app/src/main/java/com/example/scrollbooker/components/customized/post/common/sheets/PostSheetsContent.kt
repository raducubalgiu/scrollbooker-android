package com.example.scrollbooker.components.customized.post.common.sheets

sealed class PostSheetsContent {
    data class ReviewsSheet(val postId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class CalendarSheet(val postId: Int): PostSheetsContent()
    object None: PostSheetsContent()
}