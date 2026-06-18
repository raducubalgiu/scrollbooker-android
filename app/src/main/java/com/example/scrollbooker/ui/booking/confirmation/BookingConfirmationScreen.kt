package com.example.scrollbooker.ui.booking.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.ui.booking.BookingViewModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationScreen(
    viewModel: BookingViewModel,
    bookingNavigate: BookingNavigator,
) {
    val bookingFlowState by viewModel.bookingFlowState.collectAsStateWithLifecycle()
    val selectedSlot by viewModel.selectedSlot.collectAsStateWithLifecycle()
    val selectedItems by viewModel.selectedBookingItems.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val bookingFlow = (bookingFlowState as FeatureState.Success).data

    Scaffold(
        topBar = { Header(onBack = { bookingNavigate.back() }) },
        bottomBar = {
            MainButton(
                modifier = Modifier.padding(BasePadding),
                title = stringResource(R.string.confirmReservation),
                isLoading = isSaving,
                enabled = !isSaving,
                onClick = {
                    scope.launch {
                        viewModel.createAppointment()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Background)
                .verticalScroll(rememberScrollState())
                .padding(BasePadding),
            verticalArrangement = Arrangement.spacedBy(BasePadding)
        ) {
            Column {
                Text(
                    style = headlineLarge,
                    color = OnBackground,
                    fontWeight = FontWeight.ExtraBold,
                    text = stringResource(R.string.checkDetails)
                )

                Spacer(Modifier.height(SpacingXXS))

                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.confirmReservationDetails),
                )
            }

            BookingSummary(
                owner = bookingFlow.business.owner
            )

            Text(
                text = stringResource(R.string.services),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            ConfirmServicesSection()

            CancellationPolicy()
        }
    }
}