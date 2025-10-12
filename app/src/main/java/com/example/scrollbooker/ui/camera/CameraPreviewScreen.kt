package com.example.scrollbooker.ui.camera
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.camera.components.CameraBackButton
import com.example.scrollbooker.ui.theme.BackgroundDark

@OptIn(UnstableApi::class)
@Composable
fun CameraPreviewScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit
) {
    val isPrepared by viewModel.isPrepared.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    val context = LocalContext.current
    val player = viewModel.player

    val currentOnReleasePlayer by rememberUpdatedState(viewModel::releasePlayer)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer()
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            Box(modifier = Modifier.padding(BasePadding)) {
                MainButton(
                    modifier = Modifier.padding(bottom = BasePadding),
                    onClick = {},
                    title = stringResource(R.string.nextStep),
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            CameraBackButton(onBack)

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(BasePadding))
                    .background(BackgroundDark.copy(alpha = 0.6f)),
                factory = { context ->
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
                },
                update = { playerView ->
                    playerView.player = player
                    playerView.resizeMode =
                        AspectRatioFrameLayout.RESIZE_MODE_FILL
                },
            )

            if(!isPrepared) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}