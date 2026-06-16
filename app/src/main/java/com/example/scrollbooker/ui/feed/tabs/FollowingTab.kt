package com.example.scrollbooker.ui.feed.tabs

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.post.PostPlayerWithThumbnail
import com.example.scrollbooker.components.customized.post.components.PostOverlay
import com.example.scrollbooker.components.customized.post.components.PostShimmer
import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.NavigateBookingParam
import com.example.scrollbooker.ui.feed.FollowingFeedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun FollowingTab(
    isTabActive: Boolean,
    onAction: (PostSheetActionEnum, Post) -> Unit,
    onNavigateToUserProfile: (Int, String) -> Unit,
    onNavigateToBooking: (NavigateBookingParam) -> Unit
) {
    val followingViewModel: FollowingFeedViewModel = hiltViewModel()
    val posts = followingViewModel.followingPosts.collectAsLazyPagingItems()

    val userPausedSet by followingViewModel.userPausedPostIds.collectAsStateWithLifecycle()

    val verticalPagerState = rememberPagerState { posts.itemCount }
    val settledPage by remember { derivedStateOf { verticalPagerState.settledPage } }

    // 💡 EFECTUL 1: Se ocupă STRICT de managementul ferestrei de 3 elemente și Paging (Sliding Window)
    LaunchedEffect(verticalPagerState) {
        snapshotFlow {
            val page = settledPage
            settledPage to posts.getOrNull(page)?.id
        }
            .distinctUntilChanged()
            .collectLatest { (page, postId) ->
                if (postId == null) return@collectLatest

                // Lăsăm ensureWindow doar să încarce și să descarce playerele în pool în funcție de index
                followingViewModel.ensureWindow(
                    centerIndex = page,
                    getPost = { idx -> posts.getOrNull(idx) }
                )
            }
    }

    // 💡 EFECTUL 2: Singurul „creier” pentru Play/Pause (reacționează și la schimbarea tab-ului, și la scroll vertical)
    LaunchedEffect(isTabActive, settledPage) {
        if (!isTabActive) {
            followingViewModel.stopDetailSession()
        } else {
            // Această funcție setează isTabActiveGlobal = true și apelează intern play pe settledPage
            followingViewModel.resumePlayerOnTabEnter(settledPage)
        }
    }

    // 💡 EFECTUL 3: Protecția pentru când utilizatorul iese din aplicație / blochează ecranul
    val currentOnReleasePlayer by rememberUpdatedState(followingViewModel::stopDetailSession)
    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer()
        }
    }

    val decay = rememberSplineBasedDecay<Float>()

    val snapSpec: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh,
    )

    val fling = PagerDefaults.flingBehavior(
        state = verticalPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        decayAnimationSpec = decay,
        snapAnimationSpec = snapSpec
    )

    when(posts.loadState.refresh) {
        is LoadState.Error -> ErrorScreen()
        is LoadState.Loading -> PostShimmer()
        is LoadState.NotLoading -> {
            if(posts.itemCount == 0) {
                EmptyScreen(
                    message = stringResource(R.string.notFoundPosts),
                    icon = painterResource(R.drawable.ic_video_outline),
                    color = Color.White
                )
            }

            VerticalPager(
                state = verticalPagerState,
                overscrollEffect = null,
                flingBehavior = fling,
                pageSize = PageSize.Fill,
                pageSpacing = 0.dp,
                beyondViewportPageCount = 1,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val post = posts.getOrNull(page) ?: return@VerticalPager
                val postId = post.id

                key(postId) {
                    val postActionState by followingViewModel
                        .observePostUi(postId)
                        .collectAsStateWithLifecycle()

                    val postUi = remember(post, postActionState) {
                        post.copy(
                            userActions = post.userActions.applyUiState(postActionState),
                            counters = post.counters.applyUiState(postActionState)
                        )
                    }

                    val player = followingViewModel.getPlayerForIndex(page)

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { followingViewModel.togglePlayer(page) }
                        )
                    ) {
                        if (player != null) {
                            PostPlayerWithThumbnail(
                                player = player,
                                showPlayIcon = userPausedSet.contains(postId),
                                thumbnailUrl = post.mediaFiles.first().thumbnailUrl
                            )
                        } else {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = post.mediaFiles.first().thumbnailUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }

                        PostOverlay(
                            post = postUi,
                            isSavingLike = postActionState.isSavingLike,
                            isSavingBookmark = postActionState.isSavingBookmark,
                            onAction = { onAction(it, post) },
                            onNavigateToUserProfile = onNavigateToUserProfile,
                            onLike = { followingViewModel.toggleLike(post) },
                            onBookmark = { followingViewModel.toggleBookmark(post) },
                            onNavigateToBooking = {
                                onNavigateToBooking(
                                    NavigateBookingParam(
                                        userId = post.user.id,
                                        businessId = post.businessId,
                                        businessOwnerId = post.businessOwner.id,
                                        source = "feed_following"
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}