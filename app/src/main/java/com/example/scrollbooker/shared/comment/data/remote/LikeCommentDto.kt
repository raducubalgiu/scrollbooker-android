package com.example.scrollbooker.shared.comment.data.remote

data class LikeCommentDto(
    val postId: Int,
    val commentId: Int,
    val action: LikeCommentEnum
)