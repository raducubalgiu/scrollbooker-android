package com.example.scrollbooker.ui.shared.posts.sheets.reviews
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.ui.shared.reviews.ReviewsScreen
import com.example.scrollbooker.ui.shared.reviews.ReviewsViewModel

@Composable
fun ReviewsSheet(
    userId: Int,
    onClose: () -> Unit
) {
    val viewModel: ReviewsViewModel = hiltViewModel()

    ReviewsScreen(
        viewModel = viewModel,
        userId = userId,
        onClose = onClose,
        onNavigateToReviewDetail = {}
    )
}