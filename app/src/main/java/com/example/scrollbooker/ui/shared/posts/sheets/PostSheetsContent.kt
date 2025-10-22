package com.example.scrollbooker.ui.shared.posts.sheets

sealed class PostSheetsContent {
    data class BookingsSheet(val userId: Int): PostSheetsContent()
    data class ReviewDetailsSheet(val reviewerId: Int): PostSheetsContent()
    data class ReviewsSheet(val userId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class LocationSheet(val businessId: Int?): PostSheetsContent()
    data class MoreOptionsSheet(val userId: Int): PostSheetsContent()
    object None: PostSheetsContent()
}