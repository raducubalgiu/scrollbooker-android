package com.example.scrollbooker.ui.search.sheets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchSortSheet(onClose: () -> Unit) {
    SearchSheetsHeader(
        title = "Sort",
        onClose = onClose
    )
}