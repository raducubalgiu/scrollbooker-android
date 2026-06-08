package com.example.scrollbooker.ui.booking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header

@Composable
fun BookingConfirmationScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Header(onBack = onBack)
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding).fillMaxSize()) {

        }
    }
}