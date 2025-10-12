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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import coil.Coil
import coil.request.ImageRequest
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.ui.camera.components.MediaFile
import com.example.scrollbooker.ui.camera.components.MediaFilter
import com.example.scrollbooker.ui.camera.components.MediaLibraryGridItem
import com.example.scrollbooker.ui.camera.components.queryMedia
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CameraGalleryScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit,
    filter: MediaFilter = MediaFilter.VIDEOS,
    onNavigateToCameraPreview: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val media by produceState<List<MediaFile>>(initialValue = emptyList(), filter) {
        value = withContext(Dispatchers.IO) { queryMedia(context, filter) }
    }
    val screenFull = 16
    val firstN = remember(media) { media.take(screenFull) }
    var thumbsReady by remember { mutableStateOf(false) }

    val isPrepared by viewModel.isPrepared.collectAsState()
    val isPreparingSelectedVideo by viewModel.isPreparingSelectedVideo.collectAsState()
    val navigate by rememberUpdatedState(onNavigateToCameraPreview)

    LaunchedEffect(isPrepared) {
        if(isPrepared) {
            navigate()
        }
    }

    LaunchedEffect(firstN) {
        thumbsReady = false
        if(firstN.isEmpty()) {
            thumbsReady = true
            return@LaunchedEffect
        }
        val imageLoader = Coil.imageLoader(context)
        scope.launch {
            firstN.map { item ->
                async(Dispatchers.IO) {
                    val req = ImageRequest.Builder(context)
                        .data(item.uri)
                        .size(256)
                        .allowHardware(false)
                        .build()
                    imageLoader.execute(req)
                }
            }.awaitAll()
        }
        thumbsReady = true
    }

    Scaffold(
        containerColor = BackgroundDark
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(innerPadding)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomIconButton(
                    imageVector = Icons.Default.Close,
                    tint = Color.White,
                    boxSize = 60.dp,
                    iconSize = 30.dp,
                    onClick = onBack
                )

                Text(
                    text = "Selecteaza video",
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                CustomIconButton(
                    imageVector = Icons.Default.Close,
                    onClick = {},
                    tint = Color.Transparent
                )
            }

            when {
                media.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                else ->  {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        reverseLayout = true
                    ) {
                        items(media, key = { it.id }) { item ->
                            MediaLibraryGridItem(
                                item = item,
                                isPreparing = item.uri == isPreparingSelectedVideo,
                                onSelect = {
                                    val mediaItem = MediaItem.Builder().setUri(it.uri).build()
                                    viewModel.setVideo(mediaItem)
                                    viewModel.prepareSelectedVideo()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}