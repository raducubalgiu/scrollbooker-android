@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.scrollbooker.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.Recording
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.components.customized.MediaLibraryBottomSheet.MediaFile
import com.example.scrollbooker.components.customized.MediaLibraryBottomSheet.MediaFilter
import com.example.scrollbooker.components.customized.MediaLibraryBottomSheet.MediaLibraryBottomSheet
import com.example.scrollbooker.components.customized.MediaLibraryBottomSheet.queryMedia
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.camera.components.CameraActions
import com.example.scrollbooker.ui.camera.components.CameraBackButton
import com.example.scrollbooker.ui.camera.components.CameraBottomBar
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executor


@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    onNavigateToCameraPreview: () -> Unit,
    onBack: () -> Unit
) {
    val isSheetOpen by viewModel.isSheetOpen.collectAsState()
    val isCameraMounted by viewModel.isCameraMounted.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissions = remember {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { /* handle individually if you want */ }

    val hasPerms = rememberHasAllPermissions(permissions)

    LaunchedEffect(hasPerms) {
        if (!hasPerms) { permissionLauncher.launch(permissions) }
    }

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    val controller = remember(context) {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE or
                LifecycleCameraController.VIDEO_CAPTURE
            )
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(Unit) {
        previewView.controller = controller
    }

    DisposableEffect(lifecycleOwner, hasPerms) {
        if(hasPerms) controller.bindToLifecycle(lifecycleOwner)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        if(!isCameraMounted) {
            delay(200)
            viewModel.setIsCameraMounted(true)
        }
    }

    var isRecording by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            when (newState) {
                SheetValue.Hidden -> {
                    false
                }
                else -> true
            }
        }
    )

    val bottomBarHeight = 90.dp

    if(isSheetOpen) {
        MediaLibraryBottomSheet(
            sheetState = sheetState,
            onSelect = {
                val mediaItem = MediaItem.Builder().setUri(it.uri).build()

                viewModel.setVideo(mediaItem)
                viewModel.prepareSelectedVideo()
                onNavigateToCameraPreview()
            },
            onClose = {
                viewModel.openSheet(false)
            }
        )
    }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = { CameraBottomBar(bottomBarHeight=bottomBarHeight) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(innerPadding)
        ) {
            CameraBackButton(onBack)

            if(isCameraMounted) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(BasePadding))
                        .background(BackgroundDark),
                    factory = { previewView }
                )
            }

            CameraActions(
                onSwitchCamera = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        else CameraSelector.DEFAULT_BACK_CAMERA
                },
                isRecording = isRecording,
                onRecord = { isRecording = !isRecording },
                onLongPressRecord = {},
                onOpenMediaLibrary = {
                    viewModel.openSheet(true)
                },
            )
        }
    }
}

@Composable
private fun rememberHasAllPermissions(perms: Array<String>): Boolean {
    val ctx = LocalContext.current
    return remember(perms.joinToString("|")) {
        perms.all {
            ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}

