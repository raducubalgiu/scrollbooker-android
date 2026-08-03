package com.example.scrollbooker.ui.editPost
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.camera.UserProductsSheet
import com.example.scrollbooker.ui.camera.components.CreatePostBottomBar
import com.example.scrollbooker.ui.camera.components.CreatePostHeader
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleLarge
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostContent(
    isEditMode: Boolean,
    editPostUiState: EditPostUiState.Success,
    isLoading: Boolean,
    hostState: SnackbarHostState,
    onDescriptionChange: (String) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    onConfirmSelection: (Set<Product>) -> Unit,
    onNavigateToPostPreview: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val verticalScroll = rememberScrollState()
    val previewHeight = 160.dp
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    fun handleShowSheet() {
        scope.launch {
            showSheet = true
            sheetState.show()
        }
    }

    fun handleCloseSheet() {
        scope.launch {
            sheetState.hide()
            showSheet = false
        }
    }

    if (showSheet) {
        UserProductsSheet(
            sheetState = sheetState,
            linkedProducts = editPostUiState.linkedProducts,
            userProducts = FeatureState.Success(editPostUiState.catalogProducts),
            onConfirmSelection = {
                onConfirmSelection(it)
                handleCloseSheet()
            },
            onClose = { handleCloseSheet() }
        )
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            Header(
                title = if(isEditMode) stringResource(R.string.edit)
                else stringResource(R.string.create),
                onBack = onBack
            )
        },
        bottomBar = {
            CreatePostBottomBar(
                title = if(isEditMode) stringResource(R.string.save)
                else stringResource(R.string.postNow),
                onCreate = onSave,
                isLoading = isLoading,
                isDisabled = isLoading
            )
        },
        containerColor = Background,
        snackbarHost = { CustomSnackBar(hostState = hostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(verticalScroll)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            CreatePostHeader(
                previewHeight = previewHeight,
                coverUrl = editPostUiState.coverUrl,
                coverKey = editPostUiState.coverKey,
                description = editPostUiState.description,
                onDescriptionChange = onDescriptionChange,
                onNavigateToPostPreview = onNavigateToPostPreview
            )

            Row(
                modifier = Modifier.padding(
                    top = SpacingXL,
                    bottom = SpacingM,
                    start = BasePadding,
                    end = BasePadding
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.linkedServices),
                    style = titleLarge,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                if(editPostUiState.linkedProducts.isNotEmpty()) {
                    TextButton(onClick = { handleShowSheet() }) {
                        Text(
                            text = stringResource(R.string.change),
                            style = labelLarge,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (editPostUiState.linkedProducts.isEmpty()) {
                LinkedProductsOverlay(onClick = { handleShowSheet() })
            } else {
                editPostUiState.linkedProducts.forEachIndexed { index, product ->
                    LinkedProductRow(
                        product = product,
                        onRemove = { onRemoveProduct(it) },
                    )

                    if(index < editPostUiState.linkedProducts.size - 1) {
                        HorizontalDivider(
                            thickness = 0.55.dp,
                            color = Divider
                        )
                    }
                }
            }
        }
    }
}