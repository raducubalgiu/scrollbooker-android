package com.example.scrollbooker.screens.feed.search
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun FeedSearchScreen(
    onBack: () -> Unit
) {
    Layout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        onBack = onBack
    ) {

    }
}