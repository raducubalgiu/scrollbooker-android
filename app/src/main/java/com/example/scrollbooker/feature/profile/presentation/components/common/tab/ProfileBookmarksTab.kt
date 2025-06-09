package com.example.scrollbooker.feature.profile.presentation.components.common.tab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileBookmarksTab(state: LazyListState) {
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false
    ) {
        items(50) {
            Text(text = "Bookmarks Tab")
        }
    }
}