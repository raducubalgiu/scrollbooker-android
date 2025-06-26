package com.example.scrollbooker.entity.comment.data.remote

data class LikeCommentDto(
    val postId: Int,
    val commentId: Int,
    val action: LikeCommentEnum
)