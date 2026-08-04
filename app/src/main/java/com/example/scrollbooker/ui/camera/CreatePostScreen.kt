package com.example.scrollbooker.ui.camera

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.NavigationEvent
import com.example.scrollbooker.ui.editPost.EditPostContent
import com.example.scrollbooker.ui.editPost.EditPostUiState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CreatePostScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit,
    onNavigateToPostPreview: () -> Unit,
    onPostCreated: () -> Unit
) {
    val cameraVideoUiState by viewModel.cameraVideoUiState.collectAsStateWithLifecycle()
    val editUiState by viewModel.editUiState.collectAsStateWithLifecycle()

    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val isLoading = isSaving is FeatureState.Loading

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                NavigationEvent.NavigateToProfile -> {
                    onPostCreated()
                }
            }
        }
    }

    when (val createState = editUiState) {
        is EditPostUiState.Loading -> LoadingScreen()
        is EditPostUiState.Error -> ErrorScreen()
        is EditPostUiState.Success -> {
            EditPostContent(
                isEditMode = false,
                isVideoReview = viewModel.isVideoReview,
                editPostUiState = createState.copy(
                    coverUrl = cameraVideoUiState.coverUri?.toString(),
                    coverKey = cameraVideoUiState.coverKey
                ),
                isLoading = isLoading,
                hostState = hostState,
                onDescriptionChange = { viewModel.setDescription(it) },
                onRemoveProduct = { viewModel.removeLinkedProduct(it) },
                onNavigateToPostPreview = onNavigateToPostPreview,
                onConfirmSelection = { viewModel.updateLinkedProducts(it) },
                onSave = {
                    cameraVideoUiState.selectedUri?.let { uri ->
                        viewModel.createPost(videoUri = uri)
                    }
                },
                onBack = onBack
            )
        }
    }
}

