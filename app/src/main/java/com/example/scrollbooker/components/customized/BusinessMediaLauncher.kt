package com.example.scrollbooker.components.customized

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG

enum class BusinessMediaTypeEnum {
    PHOTO,
    VIDEO
}

@OptIn(UnstableApi::class)
@Composable
fun BusinessMediaLauncher(
    type: BusinessMediaTypeEnum,
    uri: Uri?,
    height: Dp = 200.dp,
    onClick: () -> Unit,
    onClear: () -> Unit
) {
    val context = LocalContext.current
    val businessPlayer = remember(uri) {
        if(uri == null || type != BusinessMediaTypeEnum.VIDEO) null
        uri?.let {
            ExoPlayer.Builder(context)
                .build()
                .apply {
                    this.setMediaItem(MediaItem.fromUri(uri))
                    this.prepare()
                    this.playWhenReady = true
                    this.repeatMode = Player.REPEAT_MODE_ALL
                }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, businessPlayer) {
        val observer = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_START -> true
                Lifecycle.Event.ON_STOP -> businessPlayer?.playWhenReady = false
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            businessPlayer?.release()
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .clip(RoundedCornerShape(BasePadding))
        .background(SurfaceBG)
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if(uri == null) {
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = "Add",
                tint = Divider
            )
        } else {
            when(type) {
                BusinessMediaTypeEnum.PHOTO -> {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height),
                        contentScale = ContentScale.Crop
                    )
                }
                BusinessMediaTypeEnum.VIDEO -> {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height),
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = businessPlayer
                                useController = false
                            }
                        },
                        update = { playerView ->
                            playerView.player = businessPlayer
                            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                        }
                    )
                }
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onClear
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_circle_solid),
                    contentDescription = "Remove Image",
                    tint = Color.White
                )
            }
        }
    }

    Spacer(Modifier.height(SpacingS))
}