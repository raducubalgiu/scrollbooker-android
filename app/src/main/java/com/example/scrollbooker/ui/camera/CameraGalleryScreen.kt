package com.example.scrollbooker.ui.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.permission.domain.model.MediaLibraryAccess
import com.example.scrollbooker.ui.LocalAppPermissions
import com.example.scrollbooker.ui.camera.components.MediaLibraryGridItem
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CameraGalleryScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit,
    onNavigateToCameraPreview: () -> Unit
) {
    val perms = LocalAppPermissions.current
    val state by perms.state.collectAsState()
    val videos = perms.videos.collectAsLazyPagingItems()

    if(state.mediaAccess == MediaLibraryAccess.NONE) {
        // aratam CTA
        return
    }

    val cameraState by viewModel.uiState.collectAsState()

    LaunchedEffect(cameraState.isReady, cameraState.selectedUri) {
        if(cameraState.isReady && cameraState.selectedUri != null) {
            onNavigateToCameraPreview()
        }
    }

    Scaffold(
        modifier = Modifier,
        containerColor = BackgroundDark
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
            .background(Background)
        ) {
            GalleryHeader(onBack)

            Box(modifier = Modifier.fillMaxSize()) {
                when(videos.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> {
                        if(videos.itemCount == 0) {
                            MessageScreen(
                                message = stringResource(R.string.dontFoundResults),
                                icon = painterResource(R.drawable.ic_video_outline)
                            )
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                verticalArrangement = Arrangement.spacedBy(1.dp),
                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                items(videos.itemCount) { index ->
                                    val item = videos[index] ?: return@items
                                    MediaLibraryGridItem(
                                        item = item,
                                        isPreparing = item.uri == cameraState.preparingUri,
                                        onSelect = { viewModel.selectVideo(it) }
                                    )
                                }
                            }

                            when(videos.loadState.append) {
                                is LoadState.Error -> Text("A aparut o eroare")
                                is LoadState.Loading -> LoadMoreSpinner()
                                is LoadState.NotLoading -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GalleryHeader(onBack: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomIconButton(
            imageVector = Icons.Default.Close,
            tint = OnBackground,
            boxSize = 60.dp,
            onClick = onBack
        )

        Text(
            text = stringResource(R.string.selectVideo),
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = OnBackground
        )

        CustomIconButton(
            imageVector = Icons.Default.Close,
            onClick = {},
            tint = Color.Transparent
        )
    }
}