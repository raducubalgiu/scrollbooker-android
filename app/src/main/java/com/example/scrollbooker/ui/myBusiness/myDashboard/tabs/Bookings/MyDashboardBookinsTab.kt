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
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myDashboard.MyDashboardViewModel
import com.example.scrollbooker.ui.myBusiness.myDashboard.components.PeriodSelector

@Composable
fun MyDashboardBookingsTab(
    viewModel: MyDashboardViewModel
) {
    val dashboardState by viewModel.dashboardBookingState.collectAsStateWithLifecycle()
    val selectedPeriod by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val selectedDateRange by viewModel.selectedDateRange.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = viewModel::onPeriodSelected
        )

        when(val state = dashboardState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(SpacingS),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = SpacingS)
                ) {
                    MyDashboardBookingDetails(
                        dashboardBooking = state.data,
                        periodText = selectedDateRange.format()
                    )

                    MyDashboardBookingSource(sources = state.data.sources)
                }
            }
        }
    }
}
