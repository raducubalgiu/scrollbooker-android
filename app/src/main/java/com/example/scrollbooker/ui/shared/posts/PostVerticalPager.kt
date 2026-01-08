package com.example.scrollbooker.ui.shared.posts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostVerticalPager(
    tabIndex: Int,
    onAction: (PostOverlayActionEnum, Post) -> Unit,
    posts: LazyPagingItems<Post>,
    feedViewModel: FeedScreenViewModel,
    drawerState: DrawerState,
    isDrawerOpen: Boolean = false,
    feedNavigate: FeedNavigator
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val showBottomBar by feedViewModel.showBottomBar.collectAsStateWithLifecycle()
    //val currentPost by feedViewModel.currentPost(tabIndex).collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { posts.itemCount })
    val currentOnReleasePlayer by rememberUpdatedState(playerViewModel::releasePlayer)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer(posts.getOrNull(pagerState.currentPage)?.id)
        }
    }

//    LaunchedEffect(drawerState.currentValue) {
//        snapshotFlow { drawerState.currentValue }
//            .collectLatest { drawerValue ->
//                currentPost?.id?.let {
//                    if (drawerValue == DrawerValue.Open) {
//                        playerViewModel.pauseIfPlaying(it)
//                    } else {
//                        playerViewModel.resumeIfPlaying(it)
//                    }
//                }
//            }
//    }

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
                }
                //feedViewModel.updateCurrentPost(tabIndex, post)
            }
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 90.dp),
        overscrollEffect = null,
        pageSize = PageSize.Fill,
        pageSpacing = 0.dp,
        beyondViewportPageCount = 1
    ) { page -> posts[page]?.let { post ->

        key(post.id) {
            val postActionState by feedViewModel
                .observePostUi(post.id)
                .collectAsStateWithLifecycle()

//                PostView(
//                    postActionState = postActionState,
//                    playerViewModel = playerViewModel,
//                    post = post,
//                    onAction = onAction,
//                    feedNavigate = feedNavigate,
//                    isDrawerOpen = isDrawerOpen,
//                    showBottomBar = showBottomBar,
//                    onShowBottomBar = { feedViewModel.toggleBottomBar() }
//                )
            }
        }
    }
}