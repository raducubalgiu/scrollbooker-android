package com.example.scrollbooker.ui.reviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.navigation.navigators.ReviewsDetailParam
import com.example.scrollbooker.navigation.navigators.ReviewsNavigator
import com.example.scrollbooker.ui.theme.Background

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel,
    reviewsNavigate: ReviewsNavigator
) {
    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.reviews),
                onBack = { reviewsNavigate.back() }
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            ReviewsSection(
                viewModel = viewModel,
                businessId = viewModel.businessId,
                employeeId = viewModel.employeeId,
                onNavigateToVideoReviewDetail = { index ->
                    reviewsNavigate.toReviewDetail(
                        ReviewsDetailParam(
                            reviewTab = ReviewsViewModel.ReviewsTab.VIDEO.key,
                            reviewIndex = index
                        )
                    )
                }
            )
        }
    }
}
