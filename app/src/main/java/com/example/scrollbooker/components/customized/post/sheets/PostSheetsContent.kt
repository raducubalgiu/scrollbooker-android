package com.example.scrollbooker.components.customized.post.sheets

sealed class PostSheetsContent {
    data class LinkedProductsSheet(
        val postId: Int
    ): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class MoreSheet(val postId: Int): PostSheetsContent()
    data class StatisticsSheet(val postId: Int): PostSheetsContent()
    data class DeletePostSheet(val postId: Int): PostSheetsContent()
    object None: PostSheetsContent()
}