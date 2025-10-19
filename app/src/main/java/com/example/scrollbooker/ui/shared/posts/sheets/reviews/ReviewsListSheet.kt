package com.example.scrollbooker.ui.shared.posts.sheets.reviews
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.ui.shared.reviews.ReviewsScreen
import com.example.scrollbooker.ui.shared.reviews.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ReviewsListSheet(
    userId: Int,
    onClose: () -> Unit
) {
    val viewModel: ReviewsViewModel = hiltViewModel()

    Column {
        SheetHeader(
            title = stringResource(R.string.reviews),
            onClose = onClose
        )

        ReviewsScreen(
            viewModel = viewModel,
            userId = userId
        )
    }
}