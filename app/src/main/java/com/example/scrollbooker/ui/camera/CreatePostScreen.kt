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
import com.example.scrollbooker.ui.post.EditPostContent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CreatePostScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit,
    onNavigateToPostPreview: () -> Unit,
    onPostCreated: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val createUiState by viewModel.createUiState.collectAsStateWithLifecycle()

    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val isLoading = isSaving is FeatureState.Loading

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

    when (val createState = createUiState) {
        is CreatePostUiState.Loading -> LoadingScreen()
        is CreatePostUiState.Error -> ErrorScreen()
        is CreatePostUiState.Success -> {
            EditPostContent(
                isEditMode = false,
                coverUri = state.coverUri.toString(),
                coverKey = state.coverKey,
                description = createState.description,
                linkedProducts = createState.linkedProducts,
                userProducts = createState.catalogProducts,
                isLoading = isLoading,
                hostState = hostState,
                onDescriptionChange = { viewModel.setDescription(it) },
                onRemoveProduct = { viewModel.removeLinkedProduct(it) },
                onNavigateToPostPreview = onNavigateToPostPreview,
                onConfirmSelection = { viewModel.updateLinkedProducts(it) },
                onSave = onPostCreated,
                onBack = onBack
            )
        }
    }
}

