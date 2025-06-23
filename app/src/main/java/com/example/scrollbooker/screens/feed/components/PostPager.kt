package com.example.scrollbooker.screens.feed.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.shared.post.domain.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostPager(
    posts: LazyPagingItems<Post>,
    pagerState: PagerState,
    isVisibleTab: Boolean,
    modifier: Modifier = Modifier
) {
    VerticalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
    ) { page ->
        when(posts.loadState.append) {
            is LoadState.Error -> "Something went wrong"
            is LoadState.Loading -> LoadMoreSpinner()
            is LoadState.NotLoading -> {
                val post = posts[page]
                if(post != null) {
                    Box(Modifier.fillMaxSize()) {
                        var videoHeightFraction by remember { mutableFloatStateOf(1f) }
                        val screenHeight = LocalConfiguration.current.screenHeightDp.dp - 90.dp
                        val videoHeight = screenHeight * videoHeightFraction

                        PostVideoItem(
                            post = post,
                            playWhenReady = pagerState.currentPage == page && isVisibleTab,
                            videoHeight = videoHeight,
                            modifier = modifier
                        )

                        PostOverlay(userActions = post.userActions, counters = post.counters)

                        Spacer(Modifier.height(BasePadding))
                    }
                }
            }
        }
    }
}