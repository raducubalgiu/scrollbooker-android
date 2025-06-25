package com.example.scrollbooker.components.customized.post.common

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.scrollbooker.shared.post.domain.model.Post

@OptIn(UnstableApi::class)
@Composable
fun PostItem(
    post: Post,
    playWhenReady: Boolean,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit
) {
    val context = LocalContext.current
    val url = post.mediaFiles.first().url


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
////        PostOverlay(
////            userActions = post.userActions,
////            counters = post.counters,
////            onOpenReviews = onOpenReviews,
////            onOpenComments = onOpenComments,
////        )
////
////        Spacer(Modifier.height(BasePadding))
//    }
}