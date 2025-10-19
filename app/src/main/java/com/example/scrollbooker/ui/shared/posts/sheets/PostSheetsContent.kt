package com.example.scrollbooker.ui.shared.posts.sheets

sealed class PostSheetsContent {
    data class ReviewsSheet(val userId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class CalendarSheet(val userId: Int): PostSheetsContent()
    data class LocationSheet(val businessId: Int?): PostSheetsContent()
    data class ProductsSheet(val userId: Int): PostSheetsContent()
    data class ReviewDetailsSheet(val reviewerId: Int): PostSheetsContent()
    data class MoreOptionsSheet(val userId: Int): PostSheetsContent()
    object None: PostSheetsContent()
}