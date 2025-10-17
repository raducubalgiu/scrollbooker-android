package com.example.scrollbooker.entity.social.comment.domain.model

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
    //val createdAt: Instant,
)

data class CommentUser(
    val id: Int,
    val username: String,
    val fullname: String,
    val avatar: String?
)