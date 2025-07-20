package com.example.scrollbooker.screens.feed

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.post.domain.model.Post
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.modules.posts.PostsPagerViewModel
import com.example.scrollbooker.modules.posts.components.PostOverlay

@Composable
fun FeedVideo(
    posts: Flow<PagingData<Post>>,
    isVisibleTab: Boolean
) {
    val lazyPagingItems = posts.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        pageCount = { lazyPagingItems.itemCount }
    )

    Box(Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) { page ->
            when(lazyPagingItems.loadState.refresh) {
                is LoadState.Error -> "Something went wrong"
                is LoadState.Loading -> LoadMoreSpinner()
                is LoadState.NotLoading -> {
                    lazyPagingItems[page]?.let { post ->
                        VideoPlayer(
                            post = post,
                            shouldPlay = page == pagerState.currentPage && isVisibleTab
                        )
                    } ?: run { Text("Loading...") }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    post: Post,
    shouldPlay: Boolean
) {
    val pagerViewModel: PostsPagerViewModel = hiltViewModel()
    val context = LocalContext.current

    val exoPlayer = remember(post.id) {
        ExoPlayer.Builder(context).build().apply {
            val url = post.mediaFiles.first().url
            setMediaItem(MediaItem.fromUri(url))
            prepare()
        }
    }

    LaunchedEffect(post.id) {
        pagerViewModel.setInitialState(
            postId = post.id,
            isLiked = post.userActions.isLiked,
            likeCount = post.counters.likeCount,
            isBookmarked = post.userActions.isBookmarked,
            bookmarkCount = post.counters.bookmarkCount
        )
    }

    val interactionState by pagerViewModel.interactionState(post.id).collectAsState()

    PostOverlay(
        interactionState = interactionState,
        counters = post.counters,
        onLike = { pagerViewModel.toggleLike(post.id) },
        onBookmark = { pagerViewModel.toggleBookmark(post.id) },
        onOpenReviews = {},
        onOpenComments = {},
        onOpenCalendar = {},
        onOpenLocation = {}
    )

    LaunchedEffect(shouldPlay) {
        if(shouldPlay) {
            exoPlayer.playWhenReady = true
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(post.id) {
        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
        }
    }
}