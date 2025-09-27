@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.scrollbooker.ui.camera

import android.Manifest
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.camera.components.RecordButton
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor

@Composable
fun CameraScreen(
    onBack: () -> Unit,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
    val scope = rememberCoroutineScope()

    var recording: Recording? = null

    // --- Permissions (modern) ---
    val permissions = remember {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO, // necesar doar pt VIDEO_CAPTURE
        )
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
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nu ai permisiuni")
        }

        return
        // Aici vom arata un UI de request permissions
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

    LaunchedEffect(Unit) {
        scope.launch {
            delay(200)
            isCameraReady = true
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth().height(90.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .padding(
                            top = SpacingXL,
                            start = BasePadding,
                            end = BasePadding
                        )
                        .clickable {},
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "15s",
                        style = titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .padding(
                            top = SpacingXL,
                            start = BasePadding,
                            end = BasePadding
                        )
                        .clickable {},
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "30s",
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Divider,
                        fontSize = 18.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .padding(
                            top = SpacingXL,
                            start = BasePadding,
                            end = BasePadding
                        )
                        .clickable {},
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "60s",
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Divider,
                        fontSize = 18.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        // --- UI overlay ---
        Box(modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(innerPadding)
        ) {
            Box(modifier = Modifier
                .zIndex(5f)
                .clickable { onBack() }
            ) {
                Box(
                    modifier = Modifier.padding(BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Back Button",
                        tint = Color(0xFFE0E0E0),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(
                        onClick = onBack,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    tint = OnBackground,
                    contentDescription = null
                )
            }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        controller.cameraSelector =
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            else CameraSelector.DEFAULT_BACK_CAMERA
                    }
                ) {
                    Icon(Icons.Default.Cameraswitch, contentDescription = "Flip camera")
                }

                RecordButton(
                    isRecording = true,
                    onTap = {},
                    onLongPress = {}
                )

                IconButton(
                    onClick = {
                        takePhoto(
                            controller = controller,
                            executor = mainExecutor,
                            onPhotoTaken = viewModel::onTakePhoto
                        )
                    }
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Take Photo")
                }
            }
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

