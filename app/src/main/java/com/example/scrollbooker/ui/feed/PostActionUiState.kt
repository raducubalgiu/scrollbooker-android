package com.example.scrollbooker.ui.feed

data class PostActionUiState(
    // flags - tri state
    val isLiked: Boolean? = null,
    val isBookmarked: Boolean? = null,
    val isReposted: Boolean? = null,

    // counters (delta peste backend)
    val likeCountDelta: Int = 0,
    val bookmarkCountDelta: Int = 0,
    val reportCountDelta: Int = 0,
    val commentCountDelta: Int = 0,

    // loading pe actiuni
    val isSavingLike: Boolean = false,
    val isSavingBookmark: Boolean = false,
    val isSavingRepost: Boolean = false
) {
    companion object { val EMPTY = PostActionUiState() }
}