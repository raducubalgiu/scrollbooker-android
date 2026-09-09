package com.example.scrollbooker.ui.profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.components.customized.post.PostDetailSkeleton
import com.example.scrollbooker.components.customized.post.PostVerticalPager
import com.example.scrollbooker.components.customized.post.handlePostSheetAction
import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsHost
import com.example.scrollbooker.components.customized.post.sheets.rememberPostSheetsState
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.data.mappers.withMediaStatus
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseProfilePostDetailScreen(
    detailScopeKey: String,
    viewModel: ProfilePostDetailViewModelContract,
    postTabKey: String,
    postIndex: Int,
    profileNavigate: ProfileNavigator,
) {
    val userPausedSet by viewModel.userPausedPostIds.collectAsStateWithLifecycle()
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

    fun patchedPost(idx: Int): Post? {
        val raw = if (idx in 0 until posts.itemCount) posts.peek(idx) else null
        return raw?.withMediaStatus(viewModel.observePostUi(raw.id).value.mediaStatus)
    }

    DisposableEffect(detailScopeKey) {
        viewModel.setDetailScreenActive(true, detailScopeKey, postIndex) { idx -> patchedPost(idx) }
        onDispose {
            viewModel.setDetailScreenActive(false, detailScopeKey, postIndex) { idx -> patchedPost(idx) }
            viewModel.onDetailSessionFinished(detailScopeKey)
        }
    }

    val postSheets = rememberPostSheetsState()

    PostSheetsHost(
        state = postSheets,
        onNavigateToBooking = { product ->
            val source = when (PostTabEnum.fromKey(postTabKey)) {
                PostTabEnum.POSTS -> BookingSourceEnum.PROFILE_GRID_POST_DETAIL
                PostTabEnum.BOOKMARKS -> BookingSourceEnum.PROFILE_BOOKMARKS_POST_DETAIL
                null -> BookingSourceEnum.PROFILE_GRID_POST_DETAIL
            }

            profileNavigate.toBookingFromProduct(product, source)
        },
        onNavigateToEditPost = { profileNavigate.toEditPost(it) },
        onPostDeleted = { viewModel.refreshPagedContent() },
        onNavigateToUserProfile = { profileNavigate.toUserProfile(it) }
    )

    val hasData = remember(posts.itemCount) { posts.itemCount > 0 }

    if (!hasData) {
        PostDetailSkeleton(
            title = title,
            onBack = { profileNavigate.back() }
        )
        return
    }

    key(postIndex) {
        val pagerState = rememberPagerState(initialPage = postIndex) { posts.itemCount }

        val currentPost by remember(pagerState) {
            derivedStateOf {
                val currentPage = pagerState.currentPage
                if (currentPage in 0 until posts.itemCount) posts.peek(currentPage) else null
            }
        }

        val fallbackPostUi = remember { MutableStateFlow(PostActionUiState.EMPTY) }
        val currentPostActionState by (currentPost?.id?.let(viewModel::observePostUi) ?: fallbackPostUi)
            .collectAsStateWithLifecycle()

        LaunchedEffect(pagerState.settledPage, currentPostActionState.mediaStatus) {
            viewModel.onPostSettled(
                scopeKey = detailScopeKey,
                index = pagerState.settledPage,
                getPost = { idx -> patchedPost(idx) }
            )
        }

        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                Header(
                    onBack = { profileNavigate.back() },
                    title = title,
                    icon = Icons.Default.Close,
                    iconSize = 30.dp,
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundDark)
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                PostVerticalPager(
                    pagerState = pagerState,
                    items = posts,
                    getPlayer = { p -> viewModel.getPlayerForIndex(detailScopeKey, p) },
                    userPausedPostIds = userPausedSet,
                    observePostUi = viewModel::observePostUi,
                    onTogglePlay = { p -> viewModel.togglePlayPause(detailScopeKey, p) },
                    onLike = { viewModel.toggleLike(it) },
                    onBookmark = { viewModel.toggleBookmark(it) },
                    onShare = { post, channel -> viewModel.sharePost(post, channel) },
                    onAction = { action, post -> handlePostSheetAction(action, post, postSheets::open) },
                    onNavigateToUserProfile = { profileNavigate.toUserProfile(it) },
                    onNavigateToReviews = { profileNavigate.toReviews(it) },
                    showBookButton = false,
                    modifier = Modifier.weight(1f)
                )

                MainButton(
                    modifier = Modifier.padding(
                        vertical = SpacingS,
                        horizontal = BasePadding
                    ),
                    contentPadding = PaddingValues(12.dp),
                    onClick = {
                        currentPost?.let {
                            handlePostSheetAction(
                                action = PostSheetActionEnum.OPEN_LINKED_PRODUCTS,
                                post = it,
                                handleOpenSheet = postSheets::open
                            )
                        }
                    },
                    title = stringResource(R.string.bookNow),
                )
            }
        }
    }
}
