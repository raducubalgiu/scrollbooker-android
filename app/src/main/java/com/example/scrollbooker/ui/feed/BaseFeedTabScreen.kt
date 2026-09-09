package com.example.scrollbooker.ui.feed

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.post.PostVerticalPager
import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.ReviewsParam
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun BaseFeedTabScreen(
    posts: LazyPagingItems<Post>,
    isTabActive: Boolean,
    viewModel: FeedViewModelContract,
    onAction: (PostSheetActionEnum, Post) -> Unit,
    onNavigateToReviews: (param: ReviewsParam) -> Unit,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit
) {
    val userPausedSet by viewModel.userPausedPostIds.collectAsStateWithLifecycle()

    val verticalPagerState = rememberPagerState { posts.itemCount }
    val settledPage by remember { derivedStateOf { verticalPagerState.settledPage } }

    val scrollToTopSignal by viewModel.scrollToTopSignal.collectAsStateWithLifecycle()
    var lastHandledScrollToTopSignal by rememberSaveable { mutableIntStateOf(scrollToTopSignal) }
    LaunchedEffect(scrollToTopSignal) {
        if (scrollToTopSignal != lastHandledScrollToTopSignal) {
            lastHandledScrollToTopSignal = scrollToTopSignal
            if (verticalPagerState.pageCount > 0) {
                verticalPagerState.scrollToPage(0)
            }
        }
    }

    val currentSettledPage by rememberUpdatedState(settledPage)
    val currentViewModel by rememberUpdatedState(viewModel)

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(isTabActive, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    currentViewModel.stopDetailSession()
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (isTabActive) {
                        currentViewModel.resumePlayerOnTabEnter(currentSettledPage)
                    }
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        if (isTabActive) {
            currentViewModel.resumePlayerOnTabEnter(currentSettledPage)
        } else {
            currentViewModel.stopDetailSession()
        }

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            currentViewModel.stopDetailSession()
        }
    }

    LaunchedEffect(isTabActive, posts) {
        if (!isTabActive) return@LaunchedEffect

        snapshotFlow { posts.getOrNull(verticalPagerState.settledPage)?.id }
            .distinctUntilChanged()
            .collectLatest { postId ->
                val currentPage = verticalPagerState.settledPage

                viewModel.ensureWindow(
                    centerIndex = currentPage,
                    getPost = { idx -> posts.getOrNull(idx) }
                )

                if (postId != null) {
                    viewModel.onPageSettled(currentPage)
                }
            }
    }

    when (posts.loadState.refresh) {
        is LoadState.Error -> ErrorScreen()
        is LoadState.Loading -> LoadingScreen(color = Color.White)
        is LoadState.NotLoading -> {
            if (posts.itemCount == 0) {
                EmptyScreen(
                    message = stringResource(R.string.notFoundPosts),
                    icon = painterResource(R.drawable.ic_video_outline),
                    color = Color.White
                )
            }

            PostVerticalPager(
                pagerState = verticalPagerState,
                items = posts,
                getPlayer = { p -> viewModel.getPlayerForIndex(p) },
                userPausedPostIds = userPausedSet,
                observePostUi = viewModel::observePostUi,
                onTogglePlay = { p -> viewModel.togglePlayer(p) },
                onLike = { viewModel.toggleLike(it) },
                onBookmark = { viewModel.toggleBookmark(it) },
                onShare = { post, channel -> viewModel.sharePost(post, channel) },
                onAction = { action, post -> onAction(action, post) },
                onNavigateToUserProfile = onNavigateToUserProfile,
                onNavigateToReviews = onNavigateToReviews,
                keyByPostId = true,
            )
        }
    }
}
