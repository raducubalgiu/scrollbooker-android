package com.example.scrollbooker.ui.shared.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.shared.posts.components.PostPlayerView
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostControls
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.theme.BackgroundDark

@Composable
fun PostView(
    postActionState: PostActionUiState,
    post: Post,
    viewModel: FeedScreenViewModel,
    onAction: (PostOverlayActionEnum, Post) -> Unit,
    feedNavigate: FeedNavigator,
    isDrawerOpen: Boolean,
    showBottomBar: Boolean = false,
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

    val player = remember { viewModel.getOrCreatePlayer(post) }

    val playerState by viewModel.getPlayerState(post.id)
        .collectAsStateWithLifecycle()

    val isPlaying = playerState.isPlaying
    val showControls by remember(playerState, isDrawerOpen) {
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
                onClick = { viewModel.togglePlayer(post.id) }
            )
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = post.mediaFiles.first().thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        PostPlayerView(player)

//        PostControls(
//            player = player,
//            post = post,
//            isPlaying = isPlaying,
//            visible = showControls
//        )

        PostOverlay(
            post = postUi,
            postActionState = postActionState,
            onAction = stableOnAction,
            showBottomBar = showBottomBar,
            onShowBottomBar = onShowBottomBar,
            onNavigateToUserProfile = { feedNavigate.toUserProfile(it) }
        )
    }
}