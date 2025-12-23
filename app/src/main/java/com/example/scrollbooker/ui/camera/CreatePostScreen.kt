package com.example.scrollbooker.ui.camera

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.headers.Header
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
    val state by viewModel.uiState.collectAsState()
    val description by viewModel.description.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val focusManager = LocalFocusManager.current
    val verticalScroll = rememberScrollState()

    val previewHeight = 160.dp

    Scaffold(
        topBar = { Header(onBack=onBack) },
        bottomBar = {
            CreatePostBottomBar(
                onCreate = {
                    state.selectedUri?.let { uri ->
                        viewModel.createPost(description, uri)
                    }
                },
                isLoading = isSaving
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(verticalScroll)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
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
        }
    }
}

