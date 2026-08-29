package com.example.scrollbooker.entity.social.comment.domain.model

import org.threeten.bp.ZonedDateTime

data class Comment(
    val id: Int,
    val text: String,
    val user: CommentUser,
    val postId: Int,
    val repliesCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val likedByPostAuthor: Boolean,
    val parentId: Int?,
    val replyToCommentId: Int?,
    val createdAt: ZonedDateTime,
)

data class CommentUser(
    val id: Int,
    val username: String,
    val fullname: String,
    val avatar: String?,
    val profession: String
)