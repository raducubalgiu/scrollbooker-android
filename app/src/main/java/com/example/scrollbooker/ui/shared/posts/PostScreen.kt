package com.example.scrollbooker.ui.shared.posts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.shared.posts.components.PostShimmer
import com.example.scrollbooker.ui.theme.BackgroundDark

@Composable
fun PostScreen(
    posts: LazyPagingItems<Post>,
    feedViewModel: FeedScreenViewModel,
    drawerState: DrawerState,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: () -> Unit,
    feedNavigate: FeedNavigator
) {
    val refreshState = posts.loadState.refresh

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundDark)
    ) {
        when (refreshState) {
            is LoadState.Error -> ErrorScreen()
            is LoadState.Loading -> PostShimmer()
            is LoadState.NotLoading -> {
                PostVerticalPager(
                    posts = posts,
                    feedViewModel = feedViewModel,
                    drawerState = drawerState,
                    shouldDisplayBottomBar = shouldDisplayBottomBar,
                    onShowBottomBar = onShowBottomBar,
                    isDrawerOpen = drawerState.isOpen,
                    feedNavigate = feedNavigate
                )
            }
        }
    }
}