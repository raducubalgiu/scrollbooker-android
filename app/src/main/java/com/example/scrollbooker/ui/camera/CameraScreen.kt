package com.example.scrollbooker.ui.camera
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.scrollbooker.ui.camera.components.CameraActions
import com.example.scrollbooker.ui.camera.components.CameraContent
import com.example.scrollbooker.ui.theme.BackgroundDark

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    onNavigateToCameraGallery: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var permissionState by remember { mutableStateOf(MediaPermissionState.CHECKING) }
    val mediaThumbUri by viewModel.mediaThumbUri.collectAsState()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val newState = MediaPermissionHelper.checkMediaPermissionState(context)
                permissionState = newState

                if (newState == MediaPermissionState.GRANTED || newState == MediaPermissionState.PARTIAL) {
                    viewModel.loadMediaThumb()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val newState = MediaPermissionHelper.evaluateMediaPermissionState(context, grants)
        permissionState = newState

        if (newState == MediaPermissionState.GRANTED || newState == MediaPermissionState.PARTIAL) {
            viewModel.loadMediaThumb()
        }
    }

    val requestMediaPermissions = remember {
        {
            val permissions = MediaPermissionHelper.getMediaPermissions()
            requestPermissionLauncher.launch(permissions)
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

    val canOpenLibrary = permissionState == MediaPermissionState.GRANTED ||
            permissionState == MediaPermissionState.PARTIAL
    val showSettingsCta = permissionState == MediaPermissionState.DENIED_PERMANENTLY

    Scaffold(
        containerColor = BackgroundDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(innerPadding)
        ) {
            CameraContent(
                onBack = onBack,
                showSettingsCta = showSettingsCta,
                openAppSettings = openAppSettings
            )

            CameraActions(
                mediaThumbUri = mediaThumbUri?.toUri(),
                onMediaThumbClick = {
                    when {
                        canOpenLibrary -> onNavigateToCameraGallery()
                        showSettingsCta -> openAppSettings()
                        else -> requestMediaPermissions()
                    }
                },
                onSwitchCamera = { },
                isRecording = false,
                onRecord = { },
                onLongPressRecord = { }
            )
        }
    }
}



