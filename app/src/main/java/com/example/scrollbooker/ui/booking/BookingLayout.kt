package com.example.scrollbooker.ui.booking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header

@Composable
fun BookingLayout(
    modifier: Modifier = Modifier,
    title: String = "",
    onBack: () -> Unit,
    onNext: () -> Unit,
    bookingTotals: BookingTotals,
    displayBottomBar: Boolean,
    isEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Header(
                title = title,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                content()
            }

            BookingBottomBar(
                bookingTotals = bookingTotals,
                isVisible = displayBottomBar,
                isEnabled = isEnabled,
                onNext = onNext
            )
        }
    }
}