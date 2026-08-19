package com.example.scrollbooker.ui.myBusiness.myDashboard.tabs.Bookings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.myBusiness.myDashboard.MyDashboardViewModel
import com.example.scrollbooker.ui.myBusiness.myDashboard.components.PeriodSelector

data class AppointmentSource(
    val source: BookingSourceEnum,
    val value: String,
    val progress: Float
)

@Composable
fun MyDashboardBookingsTab(
    viewModel: MyDashboardViewModel
) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val selectedDateRange by viewModel.selectedDateRange.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = viewModel::onPeriodSelected
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(SpacingS),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SpacingS)
        ) {
            MyDashboardBookingDetails(periodText = selectedDateRange.format())
            MyDashboardBookingSource()
        }
    }
}
