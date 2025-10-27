package com.example.scrollbooker.ui.search.sheets

import androidx.compose.runtime.Composable
import com.example.scrollbooker.ui.search.SearchViewModel

@Composable
fun SearchSortSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit
) {
    SearchSheetsHeader(
        title = "Sort",
        onClose = onClose
    )
}