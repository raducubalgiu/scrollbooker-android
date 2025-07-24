package com.example.scrollbooker.ui.sharedModules.posts.components
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.sharedModules.posts.PostsPagerViewModel
import com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay.PostOverlayActionEnum

@OptIn(UnstableApi::class)
@Composable
fun PostItem(
    viewModel: PostsPagerViewModel,
    post: Post,
    playWhenReady: Boolean,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenLocation: () -> Unit
) {
    val context = LocalContext.current
    val url = post.mediaFiles.first().url

//    val exoPlayer = remember {
//        ExoPlayer
//            .Builder(context)
//            .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(context)))
//            .build()
//            .apply {
//                setMediaItem(MediaItem.fromUri(url))
//                repeatMode = Player.REPEAT_MODE_ONE
//                prepare()
//            }
//    }
//
//    LaunchedEffect(playWhenReady) {
//        exoPlayer.playWhenReady = playWhenReady
//        //exoPlayer.playWhenReady = false
//    }

    val interactionState by viewModel.interactionState(post.id).collectAsState()

//    DisposableEffect(post.id) {
//        onDispose {
//            exoPlayer.stop()
//            exoPlayer.release()
//        }
//    }

    Box(Modifier.fillMaxSize()) {
//        AndroidView(
//            factory = {
//                PlayerView(context).apply {
//                    player = exoPlayer
//                    useController = false
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
//                    layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                    )
//                }
//            },
//            modifier = Modifier.fillMaxSize()
//        )

        PostOverlay(
            interactionState = interactionState,
            product = post.product,
            counters = post.counters,
            user = post.user,
            description = post.description,
            onAction = {
                when(it) {
                    PostOverlayActionEnum.OPEN_REVIEWS -> {
                        onOpenReviews()
                    }
                    PostOverlayActionEnum.OPEN_COMMENTS -> {
                        onOpenComments()
                    }
                    PostOverlayActionEnum.OPEN_LOCATION -> {
                        onOpenLocation()
                    }
                    PostOverlayActionEnum.OPEN_CALENDAR -> {
                        onOpenCalendar()
                    }
                    PostOverlayActionEnum.LIKE -> { viewModel.toggleLike(post.id) }
                    PostOverlayActionEnum.BOOKMARK -> { viewModel.toggleBookmark(post.id) }
                    PostOverlayActionEnum.SHARE -> {}
                }
            },
        )
    }
}