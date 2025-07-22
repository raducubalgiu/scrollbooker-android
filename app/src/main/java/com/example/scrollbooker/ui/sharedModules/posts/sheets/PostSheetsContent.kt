package com.example.scrollbooker.ui.sharedModules.posts.sheets

sealed class PostSheetsContent {
    data class ReviewsSheet(val userId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class CalendarSheet(val userId: Int): PostSheetsContent()
    data class LocationSheet(val businessId: Int?): PostSheetsContent()
    object None: PostSheetsContent()
}