package com.example.scrollbooker.components.customized.post

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.components.customized.post.components.EndOfFeedPager
import com.example.scrollbooker.components.customized.post.components.PostOverlay
import com.example.scrollbooker.components.customized.post.components.VideoScrubber
import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.core.util.sharePost
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.ReviewsParam
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PostVerticalPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    items: LazyPagingItems<Post>,
    getPlayer: (page: Int) -> Player?,
    userPausedPostIds: Set<Int>,
    observePostUi: (postId: Int) -> StateFlow<PostActionUiState>,
    onTogglePlay: (page: Int) -> Unit,
    onLike: (Post) -> Unit,
    onBookmark: (Post) -> Unit,
    onShare: (Post, ShareChannelEnum) -> Unit,
    onAction: (PostSheetActionEnum, Post) -> Unit,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit,
    onNavigateToReviews: (ReviewsParam) -> Unit,
    showBookButton: Boolean = true,
    keyByPostId: Boolean = false,
) {
    val context = LocalContext.current

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        decayAnimationSpec = rememberSplineBasedDecay(),
        snapAnimationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )

    EndOfFeedPager(
        pagerState = pagerState,
        isAtLastPage = { items.itemCount > 0 && pagerState.currentPage == items.itemCount - 1 },
        modifier = modifier
    ) { pagerModifier ->
        VerticalPager(
            state = pagerState,
            overscrollEffect = null,
            flingBehavior = fling,
            pageSize = PageSize.Fill,
            pageSpacing = 0.dp,
            beyondViewportPageCount = 1,
            modifier = pagerModifier,
        ) { page ->
            val post = items.getOrNull(page) ?: return@VerticalPager

            key(if (keyByPostId) post.id else page) {
                val player by remember(page) { derivedStateOf { getPlayer(page) } }

                val postActionState by observePostUi(post.id).collectAsStateWithLifecycle()
                val postUi = remember(post, postActionState) {
                    post.copy(
                        userActions = post.userActions.applyUiState(postActionState),
                        counters = post.counters.applyUiState(postActionState),
                        description = postActionState.description ?: post.description
                    )
                }

                var isSeeking by remember(post.id) { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTogglePlay(page) }
                        )
                ) {
                    if (player != null) {
                        PostPlayerWithThumbnail(
                            player = player!!,
                            showPlayIcon = userPausedPostIds.contains(post.id),
                            thumbnailUrl = post.mediaFiles.first().thumbnailUrl
                        )
                    } else {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = post.mediaFiles.first().thumbnailUrl,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }

                    AnimatedVisibility(
                        visible = !isSeeking,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        PostOverlay(
                            post = postUi,
                            isSavingLike = postActionState.isSavingLike,
                            isSavingBookmark = postActionState.isSavingBookmark,
                            onAction = { action -> onAction(action, post) },
                            onLike = { onLike(post) },
                            onBookmark = { onBookmark(post) },
                            onShare = {
                                sharePost(context, post) { channel -> onShare(post, channel) }
                            },
                            onNavigateToUserProfile = onNavigateToUserProfile,
                            onNavigateToReviews = onNavigateToReviews,
                            showBookButton = showBookButton,
                        )
                    }

                    if (player != null) {
                        VideoScrubber(
                            player = player!!,
                            isFocused = page == pagerState.settledPage,
                            isPaused = userPausedPostIds.contains(post.id),
                            onSeekingChanged = { isSeeking = it },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}
