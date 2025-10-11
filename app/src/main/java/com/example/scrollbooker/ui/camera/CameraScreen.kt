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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.scrollbooker.components.customized.MediaFilter
import com.example.scrollbooker.components.customized.MediaLibraryBottomSheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.camera.components.CameraActions
import com.example.scrollbooker.ui.camera.components.CameraBackButton
import com.example.scrollbooker.ui.camera.components.CameraBottomBar
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    onNavigateToCameraPreview: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
    val scope = rememberCoroutineScope()

    var recording: Recording? = null

    // --- Permissions (modern) ---
    val permissions = remember {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { /* handle individually if you want */ }

    val hasPerms = rememberHasAllPermissions(permissions)

    LaunchedEffect(hasPerms) {
        if (!hasPerms) {
            permissionLauncher.launch(permissions)
        }
    }

    if(!hasPerms) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nu ai permisiuni")
        }

        return
    }

    // --- Camera controller ---
    val controller = remember(context) {
        LifecycleCameraController(context).apply {
            // PREVIEW este implicit când folosești controller + PreviewView
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE or
                LifecycleCameraController.VIDEO_CAPTURE
            )
        }
    }

    LaunchedEffect(lifecycleOwner) {
        controller.bindToLifecycle(lifecycleOwner)
    }

    fun recordVideo(controller: LifecycleCameraController) {
        if(recording != null) {
            recording?.stop()
            recording = null
            return
        }
    }

    var isCameraReady by remember { mutableStateOf(false) }
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

    LaunchedEffect(Unit) {
        scope.launch {
            delay(200)
            isCameraReady = true
        }
    }

    var showSheetLibrary by remember { mutableStateOf(false) }

    if(showSheetLibrary) {
        MediaLibraryBottomSheet(
            sheetState = sheetState,
            initialFilter = MediaFilter.VIDEOS,
            onClose = {
                scope.launch {
                    showSheetLibrary = false
                }
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

            // Compose PreviewView wrapper (dacă ai deja CameraPreview(...), folosește-l)
            if(isCameraReady) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(BasePadding))
                        .background(BackgroundDark.copy(alpha = 0.6f)),
                    factory = { ctx ->
                        PreviewView(ctx).also { previewView ->
                            previewView.controller = controller
                            previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
                        }
                    }
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
                    showSheetLibrary = true
                },
            )
        }
    }
}

// Folosește asta dintr-un LaunchedEffect în composable și treci contextul
private fun hasAllPermissions(context: Context, perms: Array<String>): Boolean {
    return perms.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
private fun rememberHasAllPermissions(perms: Array<String>): Boolean {
    val ctx = LocalContext.current
    return remember(perms.joinToString()) { hasAllPermissions(ctx, perms) }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    executor: Executor,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                try {
                    // extensia toBitmap() vine din camera-view
                    val src = image.toBitmap()
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotated = Bitmap.createBitmap(
                        src, 0, 0, src.width, src.height, matrix, true
                    )
                    onPhotoTaken(rotated)
                } catch (t: Throwable) {
                    Timber.tag("Camera").e(t, "Failed to process image")
                } finally {
                    image.close()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Timber.tag("Camera").e(exception, "Couldn't take photo")
            }
        }
    )
}

