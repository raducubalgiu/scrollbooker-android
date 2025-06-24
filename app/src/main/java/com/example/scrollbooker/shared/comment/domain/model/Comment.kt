package com.example.scrollbooker.shared.comment.domain.model
import com.example.scrollbooker.shared.user.userSocial.domain.model.UserSocial
import org.threeten.bp.Instant

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