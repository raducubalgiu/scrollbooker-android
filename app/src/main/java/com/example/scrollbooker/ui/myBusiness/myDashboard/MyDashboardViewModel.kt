package com.example.scrollbooker.ui.myBusiness.myDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBooking
import com.example.scrollbooker.entity.dashboard.domain.useCase.GetDashboardBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyDashboardViewModel @Inject constructor(
    private val getDashboardBookingUseCase: GetDashboardBookingUseCase
) : ViewModel() {
    private val _selectedPeriod = MutableStateFlow(DashboardPeriod.SEVEN_DAYS)
    val selectedPeriod: StateFlow<DashboardPeriod> = _selectedPeriod.asStateFlow()

    val selectedDateRange: StateFlow<DashboardDateRange> = _selectedPeriod
        .map { it.getDateRange() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _selectedPeriod.value.getDateRange()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val dashboardBookingState: StateFlow<FeatureState<DashboardBooking>> = selectedDateRange
        .flatMapLatest { dateRange ->
            flow {
                emit(FeatureState.Loading)
                val result = withVisibleLoading {
                    getDashboardBookingUseCase(
                        startDate = dateRange.toApiStartDate(),
                        endDate = dateRange.toApiEndDate()
                    )
                }

                Timber.tag("Dashboard Booking").d("Result: $result")

                result.fold(
                    onSuccess = { data -> emit(FeatureState.Success(data)) },
                    onFailure = { error ->
                        Timber.tag("Dashboard Booking").e(error, "ERROR: on fetching Dashboard Booking")
                        emit(FeatureState.Error(error))
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FeatureState.Loading
        )


    fun onPeriodSelected(period: DashboardPeriod) {
        _selectedPeriod.value = period
    }
}