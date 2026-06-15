package com.example.scrollbooker.components.customized.post.sheets

sealed class PostSheetsContent {
    data class LinkedProductsSheet(val postId: Int): PostSheetsContent()
    data class ReviewsSheet(val userId: Int): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class MoreOptionsSheet(
        val postId: Int,
        val userId: Int,
        val isOwnPost: Boolean
    ): PostSheetsContent()
    object None: PostSheetsContent()
}