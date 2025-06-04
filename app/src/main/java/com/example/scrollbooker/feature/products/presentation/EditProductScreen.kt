package com.example.scrollbooker.feature.products.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.Layout

@Composable
fun EditProductScreen(
    productId: Int,
    productName: String,
    onBack: () -> Unit,
    viewModel: ProductsViewModel
) {
    Layout(
        headerTitle = productName,
        onBack = onBack
    ) {
        Text(text = "Product ID!!!: ${productId}")
    }
}