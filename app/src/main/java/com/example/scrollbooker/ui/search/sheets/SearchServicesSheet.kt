package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchServicesSheet(onClose: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        SearchSheetsHeader(
            title = "Filtrare avansata",
            onClose = onClose
        )
    }
}