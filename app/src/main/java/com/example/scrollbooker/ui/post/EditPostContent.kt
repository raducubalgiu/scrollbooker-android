package com.example.scrollbooker.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.social.post.domain.model.PostMediaFile
import com.example.scrollbooker.ui.camera.UserProductsSheet
import com.example.scrollbooker.ui.camera.components.CreatePostBottomBar
import com.example.scrollbooker.ui.camera.components.CreatePostHeader
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleLarge
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostContent(
    description: String,
    postMedia: PostMediaFile?,
    linkedProducts: Set<Product>,
    userProducts: UserProducts,
    isLoading: Boolean,
    hostState: SnackbarHostState,
    onDescriptionChange: (String) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    onConfirmSelection: (Set<Product>) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val verticalScroll = rememberScrollState()
    val previewHeight = 160.dp
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        UserProductsSheet(
            sheetState = sheetState,
            linkedProducts = linkedProducts,
            userProducts = FeatureState.Success(userProducts),
            onConfirmSelection = {
                onConfirmSelection(it)
                scope.launch {
                    sheetState.hide()
                    showSheet = false
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.edit),
                onBack = onBack
            )
        },
        bottomBar = {
            CreatePostBottomBar(
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
                coverUri = postMedia?.thumbnailUrl,
                coverKey = null,
                description = description,
                onDescriptionChange = onDescriptionChange,
                onNavigateToPostPreview = {}
            )

            Text(
                modifier = Modifier.padding(
                    top = SpacingXL,
                    bottom = SpacingM,
                    start = BasePadding,
                    end = BasePadding
                ),
                text = "Servicii asociate",
                style = titleLarge,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold
            )

            if (linkedProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            val cornerRadius = 16.dp.toPx()
                            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f) // Am mărit puțin dash-ul pentru vizibilitate mai bună

                            drawRoundRect(
                                color = Color.Gray.copy(alpha = 0.6f), // O nuanță discretă de gri pentru contur
                                style = Stroke(
                                    width = strokeWidth,
                                    pathEffect = dashEffect
                                ),
                                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                            )
                        }
                        // Face întreaga zonă punctată să fie interactivă la apăsare
                        .clickable {
                            showSheet = true
                            scope.launch { sheetState.show() }
                        }
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Spațiere curată între iconiță și text
                    ) {
                        // Iconița Plus decorativă care sugerează adăugarea
                        Icon(
                            imageVector = Icons.Default.Add, // Asigură-te că ai importat androidx.compose.material.icons.filled.Add
                            contentDescription = null, // null deoarece textul de jos explică deja acțiunea
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )

                        Text(
                            text = "Niciun produs atașat. Adaugă produse pentru a le permite utilizatorilor să rezerve direct din postare.", // Schimbat cu string-ul potrivit pentru produse
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                linkedProducts.forEachIndexed { index, product ->
                    LinkedProductRow(
                        product = product,
                        onDelete = { onRemoveProduct(product) },
                    )

                    if(index < linkedProducts.size - 1) {
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