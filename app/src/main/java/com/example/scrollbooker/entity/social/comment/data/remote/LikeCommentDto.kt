package com.example.scrollbooker.entity.social.comment.data.remote

data class LikeCommentDto(
    val postId: Int,
    val commentId: Int,
    val action: LikeCommentEnum
)