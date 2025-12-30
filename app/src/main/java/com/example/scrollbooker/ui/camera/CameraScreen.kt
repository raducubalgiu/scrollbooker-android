package com.example.scrollbooker.ui.camera
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.ui.camera.components.CameraActions
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.permission.domain.model.MediaLibraryAccess
import com.example.scrollbooker.ui.LocalAppPermissions
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.headlineMedium
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    onNavigateToCameraGallery: () -> Unit,
    onBack: () -> Unit
) {
    //val isCameraMounted by viewModel.isCameraMounted.collectAsState()
    //val lifecycleOwner = LocalLifecycleOwner.current

//    val permissions = remember {
//        arrayOf(Manifest.permission.RECORD_AUDIO)
//    }
//
//    val hasPerms = rememberHasAllPermissions(permissions)

//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestMultiplePermissions()
//    ) { /* handle individually if you want */ }

//    LaunchedEffect(hasPerms) {
//        if (!hasPerms) {
//            permissionLauncher.launch(permissions)
//        }
//    }
//
//    DisposableEffect(lifecycleOwner, hasPerms) {
//        if(hasPerms) viewModel.bindIfNeeded(lifecycleOwner)
//        onDispose {  }
//    }

//    val previewView = remember {
//        PreviewView(context).apply {
//            implementationMode = PreviewView.ImplementationMode.PERFORMANCE
//            scaleType = PreviewView.ScaleType.FILL_CENTER
//        }
//    }
//
//    LaunchedEffect(viewModel.cameraController) {
//        previewView.controller = viewModel.cameraController
//    }
//
//    LaunchedEffect(Unit) {
//        if(!isCameraMounted) {
//            delay(200)
//            viewModel.setIsCameraMounted(true)
//        }
//    }
//
//    var isRecording by remember { mutableStateOf(false) }
//    val bottomBarHeight = 90.dp

    val perms = LocalAppPermissions.current
    val state by perms.state.collectAsState()
    val videos = perms.videos.collectAsLazyPagingItems()

    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        perms.markMediaRequested()
        perms.refreshMedia()
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun requestMediaPermissions() {
        perms.markMediaRequested()

        val permissions = when {
            Build.VERSION.SDK_INT >= 34 -> arrayOf(
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
            Build.VERSION.SDK_INT >= 33 -> arrayOf(
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
            else -> arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        requestPermissionLauncher.launch(permissions)
    }

    val canOpenLibrary = state.mediaAccess != MediaLibraryAccess.NONE
    val showSettingsCta = state.mediaAccess == MediaLibraryAccess.NONE && state.mediaRequestOnce

    Scaffold(
        containerColor = BackgroundDark
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(innerPadding)
        ) {
            Column {
                CustomIconButton(
                    imageVector = Icons.Default.Close,
                    boxSize = 60.dp,
                    iconSize = 30.dp,
                    tint = Color.White,
                    onClick = onBack
                )

                Column(
                    modifier = Modifier
                        .padding(top = 100.dp)
                        .padding(horizontal = SpacingXL),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(R.drawable.ic_video_slash_outline),
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(Modifier.height(BasePadding))

                    Text(
                        text = "Camera este inactivă",
                        color = Color.White,
                        style = headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(SpacingS))

                    Text(
                        text = "Pentru moment camera este inactivă și nu se poate filma. Poți filma cu camera telefonului si ulterior poți uploada videoclipul alegându-l din galerie",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )


                }
            }

//            if(isCameraMounted) {
//                AndroidView(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(BasePadding))
//                        .background(BackgroundDark),
//                    factory = { previewView },
//                    update = { it.controller = viewModel.cameraController }
//                )
//            }

            CameraActions(
                mediaThumbUri = state.mediaThumbUri,
                onMediaThumbClick = {
                    if(canOpenLibrary) onNavigateToCameraGallery()
                    else requestMediaPermissions()
                },
                onSwitchCamera = { viewModel.switchCamera() },
                isRecording = false,
                onRecord = {  },
                onLongPressRecord = {},
            )
        }
    }
}

