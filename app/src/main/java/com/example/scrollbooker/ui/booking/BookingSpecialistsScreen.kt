package com.example.scrollbooker.ui.booking
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookingSpecialistsScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    onNavigateToDateTime: () -> Unit,
    onBack: () -> Unit
) {
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()

    BookingLayout(
        modifier = modifier,
        title = "Alege Specialistul",
        onBack = onBack,
        onNext = onNavigateToDateTime,
        bookingTotals = bookingTotals,
        displayBottomBar = true
    ) {
        Column(Modifier.fillMaxSize()) {

        }
    }
}