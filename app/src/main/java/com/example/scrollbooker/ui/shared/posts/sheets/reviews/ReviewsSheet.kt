package com.example.scrollbooker.ui.shared.posts.sheets.reviews
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.ui.shared.reviews.ReviewsScreen
import com.example.scrollbooker.ui.shared.reviews.ReviewsViewModel
import com.example.scrollbooker.ui.theme.Background

@Composable
fun ReviewsSheet(
    userId: Int,
    onClose: () -> Unit
) {
    val viewModel: ReviewsViewModel = hiltViewModel()

    Column(Modifier.fillMaxSize()) {
        SheetHeader(
            modifier = Modifier
                .background(Background)
                .zIndex(25f),
            title = stringResource(R.string.reviews),
            onClose = onClose
        )

        ReviewsScreen(
            viewModel = viewModel,
            userId = userId,
            onNavigateToReviewDetail = {}
        )
    }
}