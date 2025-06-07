package com.example.scrollbooker.feature.feed.presentation.tabs
import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.Video

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun FollowingTab(shouldVideoPlay: Boolean) {
    val state = rememberPagerState { 5 }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val flingBehavior = PagerDefaults.flingBehavior(
        state = state,
        snapPositionalThreshold = 0.15f,
        snapAnimationSpec = tween(durationMillis = 200)
    )

    Box(Modifier.fillMaxSize()) {
        VerticalPager(
            state = state,
            modifier = Modifier
                .height(screenHeight - 100.dp),
            beyondViewportPageCount = 1,
            flingBehavior = flingBehavior
        ) { page ->
            Box(Modifier.fillMaxSize()) {
                Video(
                    shouldVideoPlay=shouldVideoPlay
                )
            }
        }
    }
}