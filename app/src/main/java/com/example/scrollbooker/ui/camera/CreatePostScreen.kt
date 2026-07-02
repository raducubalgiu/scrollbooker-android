package com.example.scrollbooker.ui.camera

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.presentation.GlobalUploadStatus
import com.example.scrollbooker.ui.camera.components.CreatePostBottomBar
import com.example.scrollbooker.ui.camera.components.CreatePostHeader

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CreatePostScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit,
    onNavigateToPostPreview: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val uploadStatus by viewModel.uploadStatus.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current
    val verticalScroll = rememberScrollState()
    val previewHeight = 160.dp

    LaunchedEffect(uploadStatus) {
        if (uploadStatus is GlobalUploadStatus.Uploading) {
            onBack()
        }
    }

    Scaffold(
        topBar = { Header(onBack = onBack) },
        bottomBar = {
            CreatePostBottomBar(
                onCreate = {
                    state.selectedUri?.let { uri ->
                        viewModel.createPost(
                            description = description,
                            videoUri = uri
                        )
                    }
                },
                isLoading = false
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(verticalScroll)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
        ) {
            CreatePostHeader(
                previewHeight = previewHeight,
                coverUri = state.coverUri.toString(),
                coverKey = state.coverKey,
                description = description,
                onDescriptionChange = { viewModel.setDescription(it) },
                onNavigateToPostPreview = onNavigateToPostPreview
            )

            if (uploadStatus is GlobalUploadStatus.Error) {
                val errorMessage = (uploadStatus as GlobalUploadStatus.Error).message
                AlertDialog(
                    onDismissRequest = {  },
                    title = { Text("Error starting post") },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        TextButton(onClick = {  }) { Text("OK") }
                    }
                )
            }
        }
    }
}

