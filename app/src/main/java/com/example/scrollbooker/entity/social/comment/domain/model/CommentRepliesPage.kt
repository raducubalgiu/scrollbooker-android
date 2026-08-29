package com.example.scrollbooker.entity.social.comment.domain.model

data class CommentRepliesPage(
    val items: List<Comment>,
    val hasMore: Boolean
)
