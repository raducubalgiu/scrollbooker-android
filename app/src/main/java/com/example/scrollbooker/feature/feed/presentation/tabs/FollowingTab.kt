package com.example.scrollbooker.feature.feed.presentation.tabs
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.overscroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.Video

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun FollowingTab(shouldVideoPlay: Boolean) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .overscroll(null)
    ) {
        items(2) { item ->
            Video(
                shouldVideoPlay=shouldVideoPlay
            )
        }
    }
}