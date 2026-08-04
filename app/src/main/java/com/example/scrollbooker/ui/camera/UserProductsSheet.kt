package com.example.scrollbooker.ui.camera
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.customized.products.SelectableProductsList
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.profile.sheets.ScheduleShimmer
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.labelLarge
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProductsSheet(
    sheetState: SheetState,
    linkedProducts: Set<Product>,
    userProducts: FeatureState<UserProducts?>,
    onConfirmSelection: (Set<Product>) -> Unit,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var localLinkedProducts by remember(linkedProducts) {
        mutableStateOf(linkedProducts)
    }

    val bottomBarHeight = 80.dp
    val isEnabled = localLinkedProducts.isNotEmpty() && localLinkedProducts != linkedProducts

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose
    ) {
        SheetHeader(
            title = stringResource(R.string.linkedServices),
            onClose = onClose
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                when (val state = userProducts) {
                    is FeatureState.Loading -> ScheduleShimmer()
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Success -> {
                        state.data?.let {
                            SelectableProductsList(
                                listState = listState,
                                products = it,
                                selectedProducts = localLinkedProducts,
                                onSelect = { product ->
                                    localLinkedProducts =
                                        if (localLinkedProducts.contains(product)) {
                                            localLinkedProducts - product
                                        } else {
                                            localLinkedProducts + product
                                        }
                                },
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    top = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp + bottomBarHeight
                                )
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                HorizontalDivider(
                    color = Divider,
                    thickness = 0.55.dp
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bottomBarHeight),
                    color = Background
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val count = localLinkedProducts.size
                        val message = if (count == 1) "$count produs selectat" else "$count produse selectate"

                        Text(
                            text = message,
                            style = bodyLarge,
                            fontWeight = FontWeight.Medium
                        )

                        Button(
                            enabled = isEnabled,
                            onClick = {
                                scope.launch {
                                    onConfirmSelection(localLinkedProducts)
                                    sheetState.hide()
                                }
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.confirm),
                                style = labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
