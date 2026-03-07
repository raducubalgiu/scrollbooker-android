package com.example.scrollbooker.ui.camera
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
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

    var permissionState by remember { mutableStateOf(MediaPermissionState.CHECKING) }
    var mediaThumbUri by remember { mutableStateOf<String?>(null) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val newState = evaluateMediaPermissionState(context, grants)
        permissionState = newState

        if (newState == MediaPermissionState.GRANTED || newState == MediaPermissionState.PARTIAL) {
            viewModel.loadMediaThumb()
        }
    }

    LaunchedEffect(Unit) {
        permissionState = checkMediaPermissionState(context)

        if (permissionState == MediaPermissionState.GRANTED || permissionState == MediaPermissionState.PARTIAL) {
            viewModel.loadMediaThumb()
        }
    }

    val thumbState by viewModel.mediaThumbUri.collectAsState()
    LaunchedEffect(thumbState) {
        mediaThumbUri = thumbState
    }

    fun requestMediaPermissions() {
        val permissions = getMediaPermissions()
        requestPermissionLauncher.launch(permissions)
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
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
                openAppSettings = { openAppSettings() }
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

private fun getMediaPermissions(): Array<String> {
    return when {
        Build.VERSION.SDK_INT >= 34 -> arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
        )
        Build.VERSION.SDK_INT >= 33 -> arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO
        )
        else -> arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}

private fun checkMediaPermissionState(context: Context): MediaPermissionState {
    return when {
        Build.VERSION.SDK_INT >= 34 -> {
            val fullAccess = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED

            val partialAccess = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) == PackageManager.PERMISSION_GRANTED

            when {
                fullAccess -> MediaPermissionState.GRANTED
                partialAccess -> MediaPermissionState.PARTIAL
                else -> MediaPermissionState.DENIED
            }
        }
        Build.VERSION.SDK_INT >= 33 -> {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                MediaPermissionState.GRANTED
            } else {
                MediaPermissionState.DENIED
            }
        }
        else -> {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                MediaPermissionState.GRANTED
            } else {
                MediaPermissionState.DENIED
            }
        }
    }
}

private fun evaluateMediaPermissionState(
    context: Context,
    grants: Map<String, Boolean>
): MediaPermissionState {
    return when {
        Build.VERSION.SDK_INT >= 34 -> {
            val fullGranted = grants[Manifest.permission.READ_MEDIA_VIDEO] == true
            val partialGranted = grants[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] == true

            when {
                fullGranted -> MediaPermissionState.GRANTED
                partialGranted -> MediaPermissionState.PARTIAL
                grants.values.any { it == false } -> {
                    val canShowRationale =
                        (context as? android.app.Activity)?.shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_MEDIA_VIDEO
                        ) ?: false

                    if (canShowRationale) MediaPermissionState.DENIED
                    else MediaPermissionState.DENIED_PERMANENTLY
                }
                else -> MediaPermissionState.DENIED
            }
        }
        Build.VERSION.SDK_INT >= 33 -> {
            val granted = grants[Manifest.permission.READ_MEDIA_VIDEO] == true

            if (granted) {
                MediaPermissionState.GRANTED
            } else {
                val canShowRationale =
                    (context as? android.app.Activity)?.shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) ?: false

                if (canShowRationale) MediaPermissionState.DENIED
                else MediaPermissionState.DENIED_PERMANENTLY
            }
        }
        else -> {
            val granted = grants[Manifest.permission.READ_EXTERNAL_STORAGE] == true

            if (granted) {
                MediaPermissionState.GRANTED
            } else {
                val canShowRationale =
                    (context as? android.app.Activity)?.shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ?: false

                if (canShowRationale) MediaPermissionState.DENIED
                else MediaPermissionState.DENIED_PERMANENTLY
            }
        }
    }
}

