package com.example.scrollbooker.ui.shared.posts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.shared.posts.components.NotFoundPosts
import com.example.scrollbooker.ui.shared.posts.components.PostPlayerView
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostControls
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheets
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostVerticalPager(
    pagerState: PagerState,
    posts: LazyPagingItems<Post>,
    feedViewModel: FeedScreenViewModel,
    playerViewModel: PlayerViewModel,

    shouldDisplayBottomBar: Boolean = false,
    onShowBottomBar: (() -> Unit)? = null,
    isDrawerOpen: Boolean = false,
    feedNavigate: FeedNavigator
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(PostSheetsContent.None) }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        sheetContent = targetSheet
        scope.launch { sheetState.show() }
    }

    if(sheetState.isVisible) {
        PostSheets(
            sheetState = sheetState,
            sheetContent = sheetContent,
            onClose = {
                sheetContent = PostSheetsContent.None
                scope.launch { sheetState.hide() }
            },
        )
    }

    fun handleAction(action: PostOverlayActionEnum, post: Post) {
        when(action) {
            PostOverlayActionEnum.LIKE -> feedViewModel.toggleLike(post)
            PostOverlayActionEnum.BOOKMARK -> feedViewModel.toggleBookmark(post)
            PostOverlayActionEnum.REPOST -> {}
            PostOverlayActionEnum.OPEN_REVIEWS -> handleOpenSheet(PostSheetsContent.ReviewsSheet(post.user.id))
            PostOverlayActionEnum.OPEN_COMMENTS -> handleOpenSheet(PostSheetsContent.CommentsSheet(post.id))
            PostOverlayActionEnum.OPEN_LOCATION -> handleOpenSheet(PostSheetsContent.LocationSheet(post.businessId))
            PostOverlayActionEnum.OPEN_CALENDAR -> {}
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
    ) { page ->
        val post = posts[page]

        if (post != null) {
            val postActionState by feedViewModel.observePostUi(post.id).collectAsState()

            val postUi = remember(post, postActionState) {
                post.copy(
                    userActions = post.userActions.applyUiState(postActionState),
                    counters = post.counters.applyUiState(postActionState)
                )
            }

            val player = remember(post.id) {
                playerViewModel.getOrCreatePlayer(post)
            }

            val playerState by playerViewModel.getPlayerState(post.id)
                .collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundDark)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { playerViewModel.togglePlayer(post.id) }
                    )
            ) {
                PostPlayerView(player)

                PostOverlay(
                    post = postUi,
                    onAction = { handleAction(it, post) },
                    shouldDisplayBottomBar = shouldDisplayBottomBar,
                    onShowBottomBar = onShowBottomBar,
                    onNavigateToUserProfile = { feedNavigate.toUserProfile(it) },
                    onNavigateToCalendar = { feedNavigate.toCalendar(it) },
                    onNavigateToProducts = { feedNavigate.toUserProducts(userId = post.user.id) },
                )

                PostControls(
                    player = player,
                    post = post,
                    isPlaying = playerState.isPlaying,
                    visible = playerState.hasStartedPlayback &&
                        !playerState.isPlaying &&
                        !playerState.isBuffering &&
                        !isDrawerOpen
                )

                if(posts.itemCount == 0) NotFoundPosts()
            }
        }
    }
}