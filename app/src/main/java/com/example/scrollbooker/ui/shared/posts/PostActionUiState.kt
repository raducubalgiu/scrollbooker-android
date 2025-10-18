package com.example.scrollbooker.ui.shared.posts

data class PostActionUiState(
    val isLiked: Boolean? = null,
    val isBookmarked: Boolean? = null,
    val isReposted: Boolean? = null,

    val likeCountDelta: Int = 0,
    val bookmarkCountDelta: Int = 0,
    val reportCountDelta: Int = 0,
    val commentCountDelta: Int = 0,

    val isSavingLike: Boolean = false,
    val isSavingBookmark: Boolean = false,
    val isSavingRepost: Boolean = false
) {
    companion object { val EMPTY = PostActionUiState() }
}