package com.example.scrollbooker.ui.booking.services
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleLarge

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookingProductsList(
    state: LazyListState,
    serviceGroups: List<BusinessServicesWithProducts>,
    selectedBookingItems: List<SelectedBookingItem>,
    onOpenProductDetail: (Product) -> Unit,
    onSelect: (Product) -> Unit
) {
    val selectedProductIds = remember(selectedBookingItems) {
        selectedBookingItems.map { it.productId }.toSet()
    }

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        serviceGroups.forEach { group ->
            item(key = "section_${group.service.id}") {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = BasePadding),
                    text = group.service.name,
                    style = titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = OnBackground
                )
            }

            items(
                items = group.products,
                key = { product -> product.id }
            ) { product ->
                val isSelected = selectedProductIds.contains(product.id)

                ProductCard(
                    product = product,
                    isSelectable = true,
                    isSelected = isSelected,
                    onSelect = onSelect,
                    onNavigateToBooking = {},
                    onOpenProductDetail = { onOpenProductDetail(product) },
                )

                if (group.products.lastOrNull()?.id != product.id) {
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