package com.example.scrollbooker.ui.shared.posts.sheets

import com.example.scrollbooker.entity.social.post.domain.model.PostUser

sealed class PostSheetsContent {
    data class BookingsSheet(val user: PostUser, val postId: Int? = null): PostSheetsContent()
    data class ReviewDetailsSheet(val reviewerId: Int): PostSheetsContent()
    data class ReviewsSheet(val userId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class LocationSheet(val businessId: Int?): PostSheetsContent()
    data class MoreOptionsSheet(val userId: Int, val isOwnPost: Boolean): PostSheetsContent()
    data class PhoneSheet(val phone: Float): PostSheetsContent()
    object None: PostSheetsContent()
}