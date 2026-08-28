package com.example.scrollbooker.ui.editPost
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.FeatureState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    viewModel: EditPostViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val isLoading = isSaving is FeatureState.Loading

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

    LaunchedEffect(isSaving) {
        if (isSaving is FeatureState.Success) {
            onBack()
        }
    }

    when (val state = uiState) {
        is EditPostUiState.Loading -> {
            LoadingScreen()
        }
        is EditPostUiState.Error -> {
            ErrorScreen()
        }
        is EditPostUiState.Success -> {
            EditPostContent(
                isEditMode = true,
                isVideoReview = false,
                editPostUiState = state,
                isLoading = isLoading,
                hostState = hostState,
                onDescriptionChange = { viewModel.setDescription(it) },
                onRemoveProduct = { viewModel.removeLinkedProduct(it) },
                onConfirmSelection = { viewModel.updateLinkedProducts(it) },
                onNavigateToPostPreview = {},
                onNavigateToPostCover = {},
                onSave = { viewModel.editPost() },
                onBack = onBack,
            )
        }
    }
}