package com.example.scrollbooker.modules.post.common
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.scrollbooker.entity.post.domain.model.Post

@Composable
fun PostItem(
    post: Post,
    playWhenReady: Boolean,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit
) {
    val context = LocalContext.current
    val url = post.mediaFiles.first().url

    PostOverlay(
        userActions = post.userActions,
        counters = post.counters,
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