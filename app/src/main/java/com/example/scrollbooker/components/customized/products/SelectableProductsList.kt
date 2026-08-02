package com.example.scrollbooker.components.customized.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.productCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.booking.services.BookingServicesTabs
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleLarge
import kotlinx.coroutines.launch

@Composable
fun SelectableProductsList(
    listState: LazyListState,
    products: UserProducts,
    contentPadding: PaddingValues = PaddingValues(BasePadding),
    selectedProducts: Set<Product> = emptySet(),
    onSelect: (Product) -> Unit
) {
    val serviceGroups = products.data
    val scope = rememberCoroutineScope()

    val lazyItemIndexToGroupIndexMap = remember(serviceGroups) {
        mutableListOf<Int>().apply {
            serviceGroups.forEachIndexed { groupIndex, group ->
                add(groupIndex)
                if (group.products.isEmpty()) {
                    add(groupIndex)
                } else {
                    repeat(group.products.size) { add(groupIndex) }
                }
            }
        }
    }

    val groupIndexToLazyItemIndexMap = remember(serviceGroups) {
        mutableListOf<Int>().apply {
            var currentLazyIndex = 0
            serviceGroups.forEach { group ->
                add(currentLazyIndex)
                val itemsInGroup =
                    1 + if (group.products.isEmpty()) 1 else group.products.size
                currentLazyIndex += itemsInGroup
            }
        }
    }

    val activeTabIndexProvider = remember(lazyItemIndexToGroupIndexMap) {
        derivedStateOf {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty() || lazyItemIndexToGroupIndexMap.isEmpty()) {
                0
            } else {
                val firstVisibleItem = visibleItems.firstOrNull { item ->
                    item.offset <= 0 && item.offset + item.size > 0
                } ?: visibleItems.first()

                val groupIndex =
                    lazyItemIndexToGroupIndexMap.getOrNull(firstVisibleItem.index)
                        ?: 0
                groupIndex.coerceIn(0, serviceGroups.lastIndex)
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        if (serviceGroups.isNotEmpty()) {
            BookingServicesTabs(
                activeTabIndexProvider = { activeTabIndexProvider.value },
                onTabChange = { tabIndex ->
                    scope.launch {
                        val targetLazyIndex =
                            groupIndexToLazyItemIndexMap.getOrNull(tabIndex)
                                ?: 0
                        listState.animateScrollToItem(targetLazyIndex)
                    }
                },
                serviceGroups = serviceGroups
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
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

                if (group.products.isEmpty()) {
                    item(key = "empty_section_${group.service.id}") {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = BasePadding),
                            text = stringResource(R.string.notFoundServicesForThisCategory),
                            color = Color.Gray
                        )
                    }
                }

                items(
                    items = group.products,
                    key = { product -> product.id }
                ) { product ->
                    ProductCard(
                        product = product,
                        isSelectable = true,
                        isSelected = product in selectedProducts,
                        onSelect = { onSelect(it) },
                        onOpenProductDetail = {},
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
}