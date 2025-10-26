package com.example.scrollbooker.ui.search.sheets

import androidx.compose.runtime.Composable

@Composable
fun SearchRatingSheet(onClose: () -> Unit) {
    SearchSheetsHeader(
        title = "Ratings",
        onClose = onClose
    )
}