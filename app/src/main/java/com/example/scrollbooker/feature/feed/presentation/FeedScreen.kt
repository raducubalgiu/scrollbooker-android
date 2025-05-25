package com.example.scrollbooker.feature.feed.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.components.ScreenContainer
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun FeedScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(100) { item ->
            Text(text = "Item ${item}")
        }
    }
}

