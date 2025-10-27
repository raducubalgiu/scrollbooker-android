package com.example.scrollbooker.ui.search.sheets

import androidx.compose.runtime.Composable
import com.example.scrollbooker.ui.search.SearchViewModel

@Composable
fun SearchRatingSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit
) {
    SearchSheetsHeader(
        title = "Ratings",
        onClose = onClose
    )
}