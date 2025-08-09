package com.example.scrollbooker.ui.shared.userProducts
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun UserProductsScreen(
    onBack: () -> Unit
) {
    Layout(
        modifier = Modifier
            .safeDrawingPadding(),
        onBack = onBack
    ) {
        Text("USer Products")
    }
}