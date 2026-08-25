package com.example.scrollbooker.ui.reviews

data class ReviewActionUiState(
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
    val isLikedByProductOwner: Boolean = false,
    val isSavingLike: Boolean = false,
) {
    companion object { val EMPTY = ReviewActionUiState() }
}