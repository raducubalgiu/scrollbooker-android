package com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.TabsViewModel
import com.example.scrollbooker.ui.shared.products.UserProductsServiceTabs
import com.example.scrollbooker.ui.shared.products.UserProductsViewModel

@Composable
fun ProductsTab(
    productsViewModel: UserProductsViewModel,
    onSelect: (Product) -> Unit,
    userId: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            UserProductsServiceTabs(
                viewModel = productsViewModel,
                userId = userId,
                onSelect = onSelect
            )
        }
    }
}