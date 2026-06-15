package com.example.scrollbooker.ui.profile

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.customized.post.PostPlayerWithThumbnail
import com.example.scrollbooker.components.customized.post.components.PostOverlay
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun UserProfilePostDetailScreen(
    postTabKey: String,
    postIndex: Int,
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator,
) {
    val postTab = PostTabEnum.fromKey(postTabKey)
    val title = when(postTab) {
        PostTabEnum.POSTS -> stringResource(R.string.posts)
        PostTabEnum.BOOKMARKS -> stringResource(R.string.bookmarks)
        null -> ""
    }

    val posts = when(postTab) {
        PostTabEnum.POSTS -> viewModel.posts.collectAsLazyPagingItems()
        PostTabEnum.BOOKMARKS -> viewModel.bookmarks.collectAsLazyPagingItems()
        null -> error("Invalid post tab key")
    }

    if (posts.itemCount == 0) {
        Box(
            modifier = Modifier.fillMaxSize().background(BackgroundDark),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary)
        }
        return
    }

    val detailScopeKey = "USER_PROFILE_DETAIL_${postTabKey}_${viewModel.userId}"

    // Activați și curățați sesiunea hardware pe baza acestui ecran specific
    DisposableEffect(detailScopeKey) {
        viewModel.setDetailScreenActive(true, detailScopeKey, postIndex, { idx ->
            if (idx in 0 until posts.itemCount) posts.peek(idx) else null
        })
        onDispose {
            viewModel.setDetailScreenActive(false, detailScopeKey, postIndex, { idx ->
                if (idx in 0 until posts.itemCount) posts.peek(idx) else null
            })
            viewModel.onDetailSessionFinished(detailScopeKey)
        }
    }

    key(postIndex) {
        val pagerState = rememberPagerState(initialPage = postIndex) { posts.itemCount }

        LaunchedEffect(pagerState.settledPage) {
            viewModel.onPostSettled(
                scopeKey = detailScopeKey,
                index = pagerState.settledPage,
                getPost = { idx -> if (idx in 0 until posts.itemCount) posts.peek(idx) else null }
            )
        }

        val fling = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(1),
            decayAnimationSpec = rememberSplineBasedDecay(),
            snapAnimationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessHigh)
        )

        Scaffold(
            containerColor = BackgroundDark,
            topBar = { Header(onBack = onBack, title = title, icon = Icons.Default.Close, iconSize = 30.dp, withBackground = false) }
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(bottom = innerPadding.calculateBottomPadding())) {
                VerticalPager(
                    state = pagerState,
                    flingBehavior = fling,
                    pageSize = PageSize.Fill,
                    beyondViewportPageCount = 1,
                    modifier = Modifier.weight(1f),
                ) { page ->
                    val post = posts.getOrNull(page) ?: return@VerticalPager

                    val player by remember(detailScopeKey, page) {
                        derivedStateOf { viewModel.getPlayerForIndex(detailScopeKey, page) }
                    }

                    val postActionState by viewModel.observePostUi(post.id).collectAsStateWithLifecycle()
                    val postUi = remember(post, postActionState) {
                        post.copy(
                            userActions = post.userActions.applyUiState(postActionState),
                            counters = post.counters.applyUiState(postActionState)
                        )
                    }

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { viewModel.togglePlayPause(detailScopeKey, page) }
                        )
                    ) {
                        if (player != null) {
                            PostPlayerWithThumbnail(player = player!!)
                        } else {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = post.mediaFiles.first().thumbnailUrl,
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }

                        PostOverlay(
                            post = postUi,
                            isSavingLike = postActionState.isSavingLike,
                            isSavingBookmark = postActionState.isSavingBookmark,
                            showBookButton = false,
                            onAction = {},
                            onNavigateToUserProfile = { userId, username -> profileNavigate.toUserProfile(userId, username) },
                            onLike = {},
                            onBookmark = {},
                            onNavigateToBooking = {}
                        )
                    }
                }

                MainButton(
                    modifier = Modifier.padding(vertical = SpacingS, horizontal = BasePadding),
                    contentPadding = PaddingValues(12.dp),
                    onClick = {},
                    title = stringResource(R.string.bookNow),
                )
            }
        }
    }
}