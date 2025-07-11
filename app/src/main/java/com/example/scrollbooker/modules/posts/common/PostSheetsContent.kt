package com.example.scrollbooker.modules.posts.common

sealed class PostSheetsContent {
    data class ReviewsSheet(val userId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class CalendarSheet(val userId: Int): PostSheetsContent()
    object None: PostSheetsContent()
}