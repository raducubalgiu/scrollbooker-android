package com.example.scrollbooker.ui.shared.reviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header

@Composable
fun ReviewDetailScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { Header(onBack = onBack) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

        }
    }
}