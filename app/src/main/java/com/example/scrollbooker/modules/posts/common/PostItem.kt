package com.example.scrollbooker.modules.posts.common
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.modules.posts.PostsPagerViewModel
import androidx.compose.runtime.getValue

@Composable
fun PostItem(
    viewModel: PostsPagerViewModel,
    post: Post,
    playWhenReady: Boolean,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit
) {
    val context = LocalContext.current
    val url = post.mediaFiles.first().url

    LaunchedEffect(post.id) {
        viewModel.setInitialState(
            postId = post.id,
            isLiked = post.userActions.isLiked,
            likeCount = post.counters.likeCount,
            isBookmarked = post.userActions.isBookmarked,
            bookmarkCount = post.counters.bookmarkCount
        )
    }

    val interactionState by viewModel.interactionState(post.id).collectAsState()

    PostOverlay(
        interactionState = interactionState,
        counters = post.counters,
        onLike = { viewModel.toggleLike(post.id) },
        onBookmark = { viewModel.toggleBookmark(post.id) },
        onOpenReviews = onOpenReviews,
        onOpenComments = onOpenComments,
    )

//    AndroidView(
//        factory = {
//            PlayerView(context).apply {
//                this.player = viewModel.exoPlayer
//                useController = false
//                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
//                layoutParams = FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//            }
//        },
//        modifier = Modifier.fillMaxSize(),
//    )

//    Box(Modifier.fillMaxSize()) {
//        AndroidView(
//            modifier = Modifier.fillMaxSize(),
//            factory = {
//                PlayerView(context).apply {
//                    player = exoPlayer
//                    useController = false
//                    controllerShowTimeoutMs = 0
//                    controllerHideOnTouch = false
//                    controllerAutoShow = false
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
//                    layoutParams = FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                    )
//                    hideController()
//                }
//            },
//        )
//
////        PostVideo(
////            url = post.mediaFiles.first().url,
////            playWhenReady = playWhenReady,
////        )
////

////
////        Spacer(Modifier.height(BasePadding))
//    }
}