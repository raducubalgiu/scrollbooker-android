package com.example.scrollbooker.feature.feed.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun FeedScreen() {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(BasePadding)
    ) {
        items(100) { item ->
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color(0xFF121212),
                    headlineColor = Color(0xFFE0E0E0)
                ),
                headlineContent = { Text(text = "Item ${item}") }
            )
        }
    }
}