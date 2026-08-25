package com.example.scrollbooker.ui.camera

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.customized.post.components.PostOverlay
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.entity.social.post.domain.model.UserPostActions
import com.example.scrollbooker.ui.editPost.EditPostUiState
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.titleMedium
import timber.log.Timber
import kotlin.math.max

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostPreviewScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit
) {
    val cameraVideoUiState by viewModel.cameraVideoUiState.collectAsStateWithLifecycle()
    val editUiState by viewModel.editUiState.collectAsStateWithLifecycle()
    val player by viewModel.player.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val playerView = remember {
        PlayerView(context).apply {
            useController = false
            controllerAutoShow = false
            controllerShowTimeoutMs = 0
            setShowBuffering(SHOW_BUFFERING_NEVER)

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    }

    DisposableEffect(player) {
        playerView.player = player
        onDispose { playerView.player = null }
    }

    DisposableEffect(player) {
        val p = player ?: return@DisposableEffect onDispose {  }

        val listener = object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                val videoRatio =
                    (videoSize.width * videoSize.pixelWidthHeightRatio) /
                            max(1, videoSize.height).toFloat()

                playerView.resizeMode =
                    if (videoRatio >= 1f) {
                        AspectRatioFrameLayout.RESIZE_MODE_FIT
                    } else {
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    }
            }
        }
        p.addListener(listener)
        onDispose { p.removeListener(listener) }
    }

    LifecycleStartEffect(Unit) {
        viewModel.play()

        onStopOrDispose {
            viewModel.pause()
        }
    }

    val pagerState = rememberPagerState { 1 }

    val isSettled by remember {
        derivedStateOf {
            !pagerState.isScrollInProgress &&
            pagerState.currentPageOffsetFraction == 0f
        }
    }

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(Modifier.fillMaxSize().background(BackgroundDark)) {
        VerticalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            userScrollEnabled = false
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .zIndex(12f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomIconButton(
                        imageVector = Icons.Default.Close,
                        boxSize = 60.dp,
                        iconSize = 30.dp,
                        tint = Color.White,
                        onClick = onBack
                    )

                    Text(
                        text = stringResource(R.string.preview),
                        color = Color.White,
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    CustomIconButton(
                        imageVector = Icons.Default.Close,
                        boxSize = 60.dp,
                        iconSize = 30.dp,
                        tint = Color.Transparent,
                        onClick = {},
                    )
                }

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(cameraVideoUiState.coverUri)
                        .memoryCacheKey(cameraVideoUiState.coverKey)
                        .diskCacheKey(cameraVideoUiState.coverKey)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Post Grid Preview",
                    contentScale = ContentScale.Crop,
                    onError = {
                        Timber.tag("Post Grid Preview Error").e("ERROR: ${it.result.throwable.message}")
                    },
                    modifier = Modifier.fillMaxSize()
                )

                if(isSettled && cameraVideoUiState.isReady && player != null) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundDark.copy(alpha = 0.6f)),
                        factory = { playerView },
                        update = { playerView },
                    )
                }

                when(val uiState = editUiState) {
                    is EditPostUiState.Success -> {
                        PostOverlay(
                            post = Post(
                                id = 1,
                                description = uiState.description,
                                user = PostUser(
                                    id = 1,
                                    fullName = "Numele meu",
                                    username = "numele_meu",
                                    avatar = null,
                                    isFollow = false,
                                    profession = "Creator",
                                    ratingsAverage = 4.5f,
                                    ratingsCount = 100
                                ),
                                businessOwner = PostBusinessOwner(
                                    id = 1,
                                    fullName = "Numele meu",
                                    ratingsAverage = 4.5f,
                                    avatar = null
                                ),
                                employee = null,
                                userActions = UserPostActions(
                                    isLiked = false,
                                    isBookmarked = false,
                                    isReposted = false
                                ),
                                mediaFiles = emptyList(),
                                counters = PostCounters(
                                    commentCount = 0,
                                    likeCount = 0,
                                    bookmarkCount = 0,
                                    repostCount = 0,
                                    shareCount = 0,
                                    bookingsCount = 0,
                                    viewsCount = 0
                                ),
                                hashtags = emptyList(),
                                isVideoReview = false,
                                isOwnPost = false,
                                businessId = 1,
                                review = null,
                                createdAt = ""
                            ),
                            isSavingLike = false,
                            isSavingBookmark = false,
                            onAction = {},
                            onNavigateToUserProfile = { _ -> {} },
                            onLike = {},
                            onBookmark = {},
                            onShare = {},
                            showBookButton = false,
                        )
                    }
                    else -> Unit
                }
            }
        }

        Column(
            modifier = Modifier.height(90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MainButton(
                modifier = Modifier.padding(
                    start = BasePadding,
                    end = BasePadding,
                    bottom = bottomPadding
                ),
                fullWidth = false,
                contentPadding = PaddingValues(SpacingM),
                onClick = {  },
                title = stringResource(R.string.postNow),
            )
        }
    }
}

