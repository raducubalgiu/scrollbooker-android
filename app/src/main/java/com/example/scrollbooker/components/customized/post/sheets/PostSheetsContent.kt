package com.example.scrollbooker.components.customized.post.sheets

import com.example.scrollbooker.entity.social.post.domain.model.Post

sealed class PostSheetsContent {
    data class LinkedProductsSheet(
        val post: Post
    ): PostSheetsContent()
    data class CommentsSheet(val postId: Int): PostSheetsContent()
    data class MoreSheet(val postId: Int): PostSheetsContent()
    data class DeletePostSheet(val postId: Int): PostSheetsContent()
    object None: PostSheetsContent()
}