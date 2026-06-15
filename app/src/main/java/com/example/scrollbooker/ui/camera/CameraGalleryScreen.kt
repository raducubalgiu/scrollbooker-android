package com.example.scrollbooker.ui.camera
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.ui.camera.components.GalleryHeader
import com.example.scrollbooker.ui.camera.components.LimitedAccessWarningBanner
import com.example.scrollbooker.ui.camera.components.MediaLibraryGridItem
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.BackgroundDark
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraGalleryScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit,
    onNavigateToCameraPreview: () -> Unit
) {
    val context = LocalContext.current
    val galleryViewModel: CameraGalleryViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    var permissionState by remember { mutableStateOf(MediaPermissionState.CHECKING) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val newState = MediaPermissionHelper.checkMediaPermissionState(context)
                permissionState = newState

                if (newState == MediaPermissionState.GRANTED || newState == MediaPermissionState.PARTIAL) {
                    galleryViewModel.refreshVideos()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(permissionState) {
        if (permissionState == MediaPermissionState.DENIED ||
            permissionState == MediaPermissionState.DENIED_PERMANENTLY) {
            onBack()
        }
    }

    val openAppSettings = remember(context) {
        {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    val canAccessLibrary = permissionState == MediaPermissionState.GRANTED ||
            permissionState == MediaPermissionState.PARTIAL

    if (!canAccessLibrary) {
        LoadingScreen()
        return
    }

    val videos = galleryViewModel.videos.collectAsLazyPagingItems()
    val cameraState by viewModel.uiState.collectAsState()

    LaunchedEffect(cameraState.isReady, cameraState.selectedUri) {
        if (cameraState.isReady && cameraState.selectedUri != null) {
            onNavigateToCameraPreview()
        }
    }

    Scaffold(
        modifier = Modifier,
        containerColor = BackgroundDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
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

            if (permissionState == MediaPermissionState.PARTIAL) {
                LimitedAccessWarningBanner(
                    onOpenSettings = openAppSettings
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (videos.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> {
                        if (videos.itemCount == 0) {
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

                            if (videos.loadState.append is LoadState.Loading) {
                                LoadMoreSpinner()
                            }
                        }
                    }
                }
            }
        }
    }
}


