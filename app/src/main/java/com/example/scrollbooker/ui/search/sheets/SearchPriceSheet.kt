package com.example.scrollbooker.ui.search.sheets

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R

@Composable
fun SearchPriceSheet(onClose: () -> Unit) {
    SearchSheetsHeader(
        title = stringResource(R.string.price),
        onClose = onClose
    )
}