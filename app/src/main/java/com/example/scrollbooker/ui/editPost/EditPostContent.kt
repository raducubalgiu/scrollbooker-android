package com.example.scrollbooker.ui.editPost
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.scrollbooker.components.customized.placeholderActionBox.PlaceholderActionBox
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.camera.UserProductsSheet
import com.example.scrollbooker.ui.camera.components.CreatePostBottomBar
import com.example.scrollbooker.ui.camera.components.CreatePostHeader
import com.example.scrollbooker.ui.camera.components.PostReviewSection
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
    isVideoReview: Boolean,
    editPostUiState: EditPostUiState.Success,
    isLoading: Boolean,
    isSaveDisabled: Boolean = false,
    hostState: SnackbarHostState,
    onDescriptionChange: (String) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    onConfirmSelection: (Set<Product>) -> Unit,
    onRatingChange: (Int) -> Unit = {},
    onReviewChange: (String) -> Unit = {},
    onNavigateToPostPreview: () -> Unit,
    onNavigateToPostCover: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val verticalScroll = rememberScrollState()
    val previewHeight = 160.dp
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    val userProducts = FeatureState.Success(editPostUiState.catalogProducts)

    fun handleShowSheet() {
        showSheet = true
        scope.launch {
            sheetState.show()
        }
    }

    fun handleCloseSheet() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showSheet = false
            }
        }
    }

    if (showSheet) {
        UserProductsSheet(
            sheetState = sheetState,
            linkedProducts = editPostUiState.linkedProducts,
            userProducts = userProducts,
            onConfirmSelection = {
                onConfirmSelection(it)
                handleCloseSheet()
            },
            onClose = { handleCloseSheet() }
        )
    }

    Box(Modifier.fillMaxSize()) {
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
                    isDisabled = isLoading || isSaveDisabled
                )
            },
            containerColor = Background,
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
                    onNavigateToPostPreview = onNavigateToPostCover
                )

                if (isVideoReview) {
                    Box(modifier = Modifier.padding(
                        top = SpacingXL,
                        start = BasePadding,
                        end = BasePadding
                    )) {
                        PostReviewSection(
                            rating = editPostUiState.rating,
                            review = editPostUiState.review,
                            onRatingChange = onRatingChange,
                            onReviewChange = onReviewChange
                        )
                    }
                } else {
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
                        PlaceholderActionBox(
                            description = stringResource(R.string.linkedServicesDescription),
                            icon = Icons.Default.Add,
                            onClick = { handleShowSheet() }
                        )
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

        CustomSnackBar(hostState = hostState)
    }
}