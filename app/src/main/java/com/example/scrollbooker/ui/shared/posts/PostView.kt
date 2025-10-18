package com.example.scrollbooker.ui.shared.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.shared.posts.components.PostPlayerView
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostControls
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.theme.BackgroundDark

@Composable
fun PostView(
    postActionState: PostActionUiState,
    post: Post,
    playerViewModel: PlayerViewModel,
    onAction: (PostOverlayActionEnum, Post) -> Unit,
    feedNavigate: FeedNavigator,
    isDrawerOpen: Boolean,
    shouldDisplayBottomBar: Boolean = false,
    onShowBottomBar: (() -> Unit)? = null,
) {
    val latestOnAction by rememberUpdatedState(onAction)

    val stableOnAction = remember(post.id) {
        {  action: PostOverlayActionEnum ->
            latestOnAction(action, post)
        }
    }

    val postUi = remember(post, postActionState) {
        post.copy(
            userActions = post.userActions.applyUiState(postActionState),
            counters = post.counters.applyUiState(postActionState)
        )
    }

    val player = remember {
        playerViewModel.getOrCreatePlayer(post)
    }

    val playerState by playerViewModel.getPlayerState(post.id)
        .collectAsState()

    val controlsVisible by remember {
        derivedStateOf {
            playerState.hasStartedPlayback &&
                    !playerState.isPlaying &&
                    !playerState.isBuffering &&
                    !isDrawerOpen
        }
    }

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
            postActionState = postActionState,
            onAction = stableOnAction,
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
            visible = controlsVisible
        )

        //if(posts.itemCount == 0) NotFoundPosts()
    }
}

//@Composable
//private fun PostPlayerHost(
//    post: Post,
//    playerViewModel: PlayerViewModel
//) {
//    key(post.id) {
//        val player = remember { playerViewModel.getOrCreatePlayer(post) }
//        DisposableEffect(Unit) {
//            onDispose { playerViewModel.releasePlayer(post.id) }
//        }
//        PostPlayerView(player)
//    }
//}