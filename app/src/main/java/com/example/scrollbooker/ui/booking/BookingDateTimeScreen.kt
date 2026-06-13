package com.example.scrollbooker.ui.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookingDateTimeScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    onNavigateToConfirmation: () -> Unit,
    onBack: () -> Unit
) {
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()

    BookingLayout(
        modifier = modifier,
        onBack = onBack,
        onNext = onNavigateToConfirmation,
        bookingTotals = bookingTotals,
        displayBottomBar = true
    ) { }
}