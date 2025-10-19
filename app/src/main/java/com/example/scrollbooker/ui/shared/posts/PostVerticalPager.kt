package com.example.scrollbooker.ui.shared.posts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.getOrNull
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheets
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostVerticalPager(
    posts: LazyPagingItems<Post>,
    feedViewModel: FeedScreenViewModel,
    drawerState: DrawerState,
    shouldDisplayBottomBar: Boolean = false,
    onShowBottomBar: (() -> Unit)? = null,
    isDrawerOpen: Boolean = false,
    feedNavigate: FeedNavigator
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val currentPost by playerViewModel.currentPost.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { posts.itemCount })
    val currentOnReleasePlayer by rememberUpdatedState(playerViewModel::releasePlayer)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer(posts.getOrNull(pagerState.currentPage)?.id)
        }
    }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(None) }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        scope.launch {
            sheetState.show()
            sheetContent = targetSheet
        }
    }

    if(sheetState.isVisible) {
        key(sheetContent) {
            PostSheets(
                sheetState = sheetState,
                sheetContent = sheetContent,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = None
                    }
                },
            )
        }
    }

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
                }
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

            PostView(
                postActionState = postActionState,
                playerViewModel = playerViewModel,
                post = post,
                onAction = { action, post ->
                    when(action) {
                        PostOverlayActionEnum.LIKE -> feedViewModel.toggleLike(post)
                        PostOverlayActionEnum.BOOKMARK -> feedViewModel.toggleBookmark(post)
                        PostOverlayActionEnum.OPEN_REVIEWS -> {
                            val id = if(post.isVideoReview) post.businessOwner.id else post.user.id
                            handleOpenSheet(ReviewsSheet(id))
                        }
                        PostOverlayActionEnum.OPEN_COMMENTS -> handleOpenSheet(CommentsSheet(post.id))
                        PostOverlayActionEnum.OPEN_LOCATION -> handleOpenSheet(LocationSheet(post.businessId))
                        PostOverlayActionEnum.OPEN_CALENDAR -> handleOpenSheet(CalendarSheet(post.user.id))
                        PostOverlayActionEnum.OPEN_PRODUCTS -> handleOpenSheet(ProductsSheet(post.user.id))
                        PostOverlayActionEnum.OPEN_REVIEW_DETAILS -> handleOpenSheet(ReviewDetailsSheet(post.user.id))
                        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> handleOpenSheet(MoreOptionsSheet(post.user.id))
                    }
                },
                feedNavigate = feedNavigate,
                isDrawerOpen = isDrawerOpen,
                shouldDisplayBottomBar = shouldDisplayBottomBar,
                onShowBottomBar = onShowBottomBar
            )
        }
    }
    }
}