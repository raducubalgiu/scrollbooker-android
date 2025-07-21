package com.example.scrollbooker.entity.social.comment.domain.model
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

data class Comment(
    val id: Int,
    val text: String,
    val user: UserSocial,
    val postId: Int,
    val repliesCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val likedByPostAuthor: Boolean,
    val parentId: Int?,
    //val createdAt: Instant,
)