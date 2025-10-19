package com.example.scrollbooker.ui.shared.posts.sheets.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.sheet.SheetHeader

@Composable
fun ProductsSheet(
    userId: Int,
    onClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SheetHeader(
            title = "Products",
            onClose = onClose
        )
    }
}