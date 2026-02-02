package com.example.scrollbooker.ui.shared.posts

data class PostActionUiState(
    val isLiked: Boolean? = null,
    val isBookmarked: Boolean? = null,
    val isReposted: Boolean? = null,

    val likesCount: Int = 0,
    val bookmarksCount: Int = 0,
    val commentsCount: Int = 0,

    val isSavingLike: Boolean = false,
    val isSavingBookmark: Boolean = false
) {
    companion object { val EMPTY = PostActionUiState() }
}