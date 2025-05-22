package com.example.scrollbooker.feature.feed.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun FeedScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(BasePadding)) {
        items(100) { item ->
            ListItem(headlineContent = { Text(text = "Item ${item}") })
        }
    }
}