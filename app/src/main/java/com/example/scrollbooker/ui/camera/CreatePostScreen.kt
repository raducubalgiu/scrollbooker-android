package com.example.scrollbooker.ui.camera

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.customized.productCard.ProductCard
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.NavigationEvent
import com.example.scrollbooker.ui.camera.components.CreatePostBottomBar
import com.example.scrollbooker.ui.camera.components.CreatePostHeader
import kotlinx.coroutines.launch

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
    val description by viewModel.description.collectAsStateWithLifecycle()
    val userProducts by viewModel.userProducts.collectAsStateWithLifecycle()
    val linkedProducts by viewModel.linkedProducts.collectAsStateWithLifecycle()

    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val isLoading = isSaving is FeatureState.Loading

    val scope = rememberCoroutineScope()
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
                NavigationEvent.NavigateToProfile -> onPostCreated()
            }
        }
    }

    val focusManager = LocalFocusManager.current
    val verticalScroll = rememberScrollState()
    val previewHeight = 160.dp

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if(sheetState.isVisible) {
        UserProductsSheet(
            sheetState = sheetState,
            linkedProducts = linkedProducts,
            userProducts = userProducts,
            onConfirmSelection = { viewModel.updateLinkedProducts(it) }
        )
    }

    Scaffold(
        topBar = { Header(onBack = onBack) },
        bottomBar = {
            CreatePostBottomBar(
                onCreate = {
                    state.selectedUri?.let { viewModel.createPost(videoUri = it) }
                },
                isLoading = isLoading,
                isDisabled = isLoading
            )
        },
        snackbarHost = { CustomSnackBar(hostState = hostState) }
    ) { innerPadding ->
        Column(modifier = Modifier
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

            if(linkedProducts.isEmpty()) {
                MainButton(
                    title = "Open Products Sheet",
                    onClick = {
                        scope.launch { sheetState.show() }
                    }
                )
            } else {
                linkedProducts.forEach {
                    ProductCard(
                        product = it,
                        isSelectable = false,
                        onSelect = {  },
                        onOpenProductDetail = {}
                    )
                }
            }
        }
    }
}

