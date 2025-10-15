package com.example.scrollbooker.ui.shared.posts

import androidx.annotation.OptIn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.util.UnstableApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.getOrNull
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.shared.posts.components.NotFoundPosts
import com.example.scrollbooker.ui.shared.posts.components.PostShimmer
import kotlinx.coroutines.flow.collectLatest

@OptIn(UnstableApi::class)
@Composable
fun PostScreen(
    feedViewModel: FeedScreenViewModel,
    posts: LazyPagingItems<Post>,
    drawerState: DrawerState,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: () -> Unit,
    feedNavigate: FeedNavigator
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val currentPost by playerViewModel.currentPost.collectAsState()

    val pagerState = rememberPagerState(pageCount = { posts.itemCount })
    val currentOnReleasePlayer by rememberUpdatedState(playerViewModel::releasePlayer)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer(posts.getOrNull(pagerState.currentPage)?.id)
        }
    }

    val refreshState = posts.loadState.refresh

    when (refreshState) {
        is LoadState.Error -> ErrorScreen()
        is LoadState.Loading -> PostShimmer()
        is LoadState.NotLoading -> {
            if(posts.itemCount == 0) NotFoundPosts()

            LaunchedEffect(drawerState.currentValue) {
                snapshotFlow { drawerState.currentValue }
                    .collectLatest { drawerValue ->
                        currentPost?.id?.let {
                            if (drawerValue == DrawerValue.Open) {
                                playerViewModel.pauseIfPlaying(it)
                            } else {
                                playerViewModel.resumeIfPlaying(it)
                            }
                        }
                    }
            }

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }
                    .collectLatest { page ->
                        val post = posts.getOrNull(page)
                        val previousPost = posts.getOrNull(page - 1)
                        val nextPost = posts.getOrNull(page + 1)

                        post?.let {
                            playerViewModel.initializePlayer(
                                post = post,
                                previousPost = previousPost,
                                nextPost = nextPost
                            )
                            //playerViewModel.pauseUnusedPlayers(visiblePostId = post.id)
                        }
                    }
            }

            PostVerticalPager(
                pagerState = pagerState,
                posts = posts,
                feedViewModel = feedViewModel,
                playerViewModel = playerViewModel,
                shouldDisplayBottomBar = shouldDisplayBottomBar,
                onShowBottomBar = onShowBottomBar,
                isDrawerOpen = drawerState.isOpen,
                feedNavigate = feedNavigate
            )
        }
    }
}