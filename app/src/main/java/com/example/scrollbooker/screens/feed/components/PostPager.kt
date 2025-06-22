package com.example.scrollbooker.screens.feed.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.shared.post.domain.model.Post

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
                    PostVideoItem(
                        post = post,
                        playWhenReady = pagerState.currentPage == page && isVisibleTab,
                        modifier = modifier
                    )
                }
            }
        }
    }
}