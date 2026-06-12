package com.example.scrollbooker.ui.booking.services
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.components.customized.ProductCard.ProductDetailSheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleLarge
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingProductsList(
    state: LazyListState,
    serviceGroups: List<BusinessServicesWithProducts>
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if(sheetState.isVisible) {
        ProductDetailSheet(
            product = selectedProduct,
            sheetState = sheetState,
            onClose = {
                scope.launch { sheetState.hide() }
            }
        )
    }

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        itemsIndexed(serviceGroups) { index, group ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = BasePadding),
                    text = group.service.name,
                    style = titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = OnBackground
                )

                group.products.forEachIndexed { index, product ->
                    ProductCard(
                        product = product,
                        isSelectable = true,
                        onNavigateToBooking = {},
                        onOpenDetailSheet = {
                            selectedProduct = it
                            scope.launch { sheetState.show() }
                        },
                        onSelect = { product ->
                            if(product.variants.size > 1) {
                                selectedProduct = product
                                scope.launch { sheetState.show() }
                            } else {
                                // Handle single variant booking flow
                            }
                        }
                    )

                    if(index < group.products.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = BasePadding),
                            color = Divider,
                            thickness = 0.55.dp
                        )
                    }
                }
            }
        }
    }
}