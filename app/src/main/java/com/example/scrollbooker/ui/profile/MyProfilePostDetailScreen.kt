@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.scrollbooker.ui.profile
import androidx.annotation.OptIn
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.theme.BackgroundDark
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.shared.player.PostPlayerWithThumbnail
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyProfilePostDetailScreen(
    postTabKey: String,
    postIndex: Int,
    viewModel: MyProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator
) {
    val postTab = PostTabEnum.fromKey(postTabKey)

    val title: String = when(postTab) {
        PostTabEnum.POSTS -> stringResource(R.string.posts)
        PostTabEnum.BOOKMARKS -> stringResource(R.string.bookmarks)
        null -> ""
    }

    val posts = when(postTab) {
        PostTabEnum.POSTS -> viewModel.posts.collectAsLazyPagingItems()
        PostTabEnum.BOOKMARKS -> viewModel.bookmarks.collectAsLazyPagingItems()
        null -> error("Invalid post tab key")
    }

    key(postIndex) {
        val pagerState = rememberPagerState(
            initialPage = postIndex
        ) { posts.itemCount }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.settledPage }
                .distinctUntilChanged()
                .collectLatest { page ->
                    viewModel.onPageSettled(page)
                    viewModel.ensureWindow(
                        centerIndex = page,
                        getPost = { idx -> posts.getOrNull(idx) }
                    )
                }
        }

        val decay = rememberSplineBasedDecay<Float>()

        val snapSpec: SpringSpec<Float> = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh,
        )

        val fling = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(1),
            decayAnimationSpec = decay,
            snapAnimationSpec = snapSpec
        )

        val currentOnReleasePlayer by rememberUpdatedState(viewModel::stopDetailSession)

        LifecycleStartEffect(true) {
            onStopOrDispose {
                currentOnReleasePlayer()
            }
        }

        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                Header(
                    modifier = Modifier.zIndex(14f),
                    onBack = onBack,
                    title = title,
                    icon = Icons.Default.Close,
                    iconSize = 30.dp,
                    withBackground = false
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                VerticalPager(
                    state = pagerState,
                    overscrollEffect = null,
                    flingBehavior = fling,
                    pageSize = PageSize.Fill,
                    pageSpacing = 0.dp,
                    beyondViewportPageCount = 1,
                    modifier = Modifier.weight(1f),
                ) { page ->
                    val post = posts.getOrNull(page) ?: return@VerticalPager
                    val player = viewModel.getPlayerForIndex(page)

                    val postActionState by viewModel
                        .observePostUi(post.id)
                        .collectAsStateWithLifecycle()

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
                            onClick = { viewModel.togglePlayer(page) }
                        )
                    ) {
                        if(player != null) {
                            PostPlayerWithThumbnail(
                                player = player,
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
                            onAction = { action -> },
                            onNavigateToUserProfile = {
                                if(it == post.user.id) onBack()
                                else profileNavigate.toUserProfile(it)
                            }
                        )
                    }
                }

                MainButton(
                    modifier = Modifier
                        .padding(
                            vertical = SpacingS,
                            horizontal = BasePadding
                        ),
                    contentPadding = PaddingValues(14.dp),
                    onClick = {},
                    title = stringResource(R.string.bookNow),
                )
            }
        }
    }
}

//private fun handlePostAction(
//    feedViewModel: ProfileLayoutViewModel,
//    action: PostOverlayActionEnum,
//    handleOpenSheet: (PostSheetsContent) -> Unit,
//    post: Post
//) {
//    when(action) {
//        PostOverlayActionEnum.OPEN_BOOKINGS -> handleOpenSheet(BookingsSheet(post.user))
//        PostOverlayActionEnum.OPEN_REVIEWS -> {
//            val id = if(post.isVideoReview) post.businessOwner.id else post.user.id
//            handleOpenSheet(ReviewsSheet(id))
//        }
//        PostOverlayActionEnum.OPEN_COMMENTS -> handleOpenSheet(CommentsSheet(post.id))
//        PostOverlayActionEnum.OPEN_LOCATION -> handleOpenSheet(LocationSheet(post.businessId))
//        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> handleOpenSheet(MoreOptionsSheet(post.user.id, post.isOwnPost))
//        PostOverlayActionEnum.OPEN_PHONE -> handleOpenSheet(PhoneSheet(0.7f))
//        PostOverlayActionEnum.LIKE -> feedViewModel.toggleLike(post)
//        PostOverlayActionEnum.BOOKMARK -> feedViewModel.toggleBookmark(post)
//    }
//}