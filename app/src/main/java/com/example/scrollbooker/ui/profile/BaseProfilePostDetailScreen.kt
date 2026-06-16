package com.example.scrollbooker.ui.profile

import androidx.compose.animation.core.Spring
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.customized.post.PostPlayerWithThumbnail
import com.example.scrollbooker.components.customized.post.components.PostDetailShimmer
import com.example.scrollbooker.components.customized.post.components.PostOverlay
import com.example.scrollbooker.components.customized.post.handlePostSheetAction
import com.example.scrollbooker.components.customized.post.sheets.PostSheets
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent.None
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseProfilePostDetailScreen(
    detailScopeKey: String,
    viewModel: ProfilePostDetailViewModelContract,
    postTabKey: String,
    postIndex: Int,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator,
) {
    val scope = rememberCoroutineScope()
    val postTab = PostTabEnum.fromKey(postTabKey)

    val title = when (postTab) {
        PostTabEnum.POSTS -> stringResource(R.string.posts)
        PostTabEnum.BOOKMARKS -> stringResource(R.string.bookmarks)
        null -> ""
    }

    val posts = when (postTab) {
        PostTabEnum.POSTS -> viewModel.posts.collectAsLazyPagingItems()
        PostTabEnum.BOOKMARKS -> viewModel.bookmarks.collectAsLazyPagingItems()
        null -> error("Invalid post tab key")
    }

    if (posts.itemCount == 0) {
        PostDetailShimmer()
        return
    }

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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(None) }

    if (sheetContent != None) {
        key(sheetContent) {
            PostSheets(
                sheetState = sheetState,
                sheetContent = sheetContent,
                onDeletePost = {},
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = None
                    }
                },
            )
        }
    }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        scope.launch {
            sheetState.show()
            sheetContent = targetSheet
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
            snapAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh
            )
        )

        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                Header(
                    onBack = onBack,
                    title = title,
                    icon = Icons.Default.Close,
                    iconSize = 30.dp,
                    withBackground = false
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { viewModel.togglePlayPause(detailScopeKey, page) }
                            )
                    ) {
                        if (player != null) {
                            PostPlayerWithThumbnail(
                                player = player!! as ExoPlayer,
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

                        PostOverlay(
                            post = postUi,
                            isSavingLike = postActionState.isSavingLike,
                            isSavingBookmark = postActionState.isSavingBookmark,
                            showBookButton = false,
                            onAction = { action ->
                                handlePostSheetAction(action, post, ::handleOpenSheet)
                            },
                            onNavigateToUserProfile = { userId, username -> profileNavigate.toUserProfile(userId, username) },
                            onLike = { viewModel.toggleLike(post) },
                            onBookmark = { viewModel.toggleBookmark(post) },
                            onNavigateToBooking = {}
                        )
                    }
                }

                MainButton(
                    modifier = Modifier.padding(
                        vertical = SpacingS,
                        horizontal = BasePadding
                    ),
                    contentPadding = PaddingValues(12.dp),
                    onClick = {},
                    title = stringResource(R.string.bookNow),
                )
            }
        }
    }
}
