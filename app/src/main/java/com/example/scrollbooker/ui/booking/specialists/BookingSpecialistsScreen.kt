package com.example.scrollbooker.ui.booking.specialists
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.ui.booking.BookingLayout
import com.example.scrollbooker.ui.booking.BookingViewModel
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSpecialistsScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    bookingNavigate: BookingNavigator
) {
    val selectedEmployeeId by viewModel.selectedEmployeeId.collectAsStateWithLifecycle()
    val selectedBookingItems by viewModel.selectedBookingItems.collectAsStateWithLifecycle()
    val bookingFlowState by viewModel.bookingFlowState.collectAsStateWithLifecycle()
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()

    LaunchedEffect(selectedBookingItems) {
        if (selectedBookingItems.isEmpty()) {
            bookingNavigate.back()
        }
    }

    BookingLayout(
        modifier = modifier,
        onBack = { bookingNavigate.back() },
        onNext = { bookingNavigate.toDateTime() },
        bookingTotals = bookingTotals,
        isEnabled = selectedEmployeeId != null,
        displayBottomBar = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.chooseSpecialist)
            )

            when(val state = bookingFlowState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    val bookingFlow = state.data

                    BookingSpecialistsList(
                        bookingFlow = bookingFlow,
                        selectedBookingItems = selectedBookingItems,
                        selectedEmployeeId = selectedEmployeeId,
                        onSetSelectedEmployeeId = { id -> id?.let { viewModel.setSelectedEmployeeId(it) } },
                        onRemoveBookingItem = { viewModel.removeBookingItem(it) }
                    )
                }
            }
        }
    }
}