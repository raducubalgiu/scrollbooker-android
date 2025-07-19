package com.example.scrollbooker.modules.posts.common
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.modules.posts.PostsPager

@Composable
fun PostsList(
    posts: LazyPagingItems<Post>,
    isVisibleTab: Boolean,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0) { posts.itemCount }

    posts.apply {
        when(loadState.refresh) {
            is LoadState.Loading -> Unit
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> PostsPager(
                posts = posts,
                pagerState = pagerState,
                isVisibleTab = isVisibleTab
            )
        }
    }
}