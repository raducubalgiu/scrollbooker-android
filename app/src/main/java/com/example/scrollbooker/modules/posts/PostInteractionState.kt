package com.example.scrollbooker.modules.posts

data class PostInteractionState(
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val isLiking: Boolean = false,

    val isBookmarked: Boolean = false,
    val bookmarkCount: Int = 0,
    val isBookmarking: Boolean = false
)