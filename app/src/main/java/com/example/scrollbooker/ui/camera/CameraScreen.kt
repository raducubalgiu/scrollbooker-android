package com.example.scrollbooker.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.camera.components.CameraActions
import com.example.scrollbooker.ui.camera.components.CameraBottomBar
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.delay

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    onNavigateToCameraGallery: () -> Unit,
    onBack: () -> Unit
) {
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
    val bottomBarHeight = 90.dp

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = { CameraBottomBar(bottomBarHeight=bottomBarHeight) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(innerPadding)
        ) {
            CustomIconButton(
                imageVector = Icons.Default.Close,
                boxSize = 60.dp,
                iconSize = 30.dp,
                tint = Color.White,
                onClick = onBack
            )

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
                onOpenMediaLibrary = onNavigateToCameraGallery,
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

