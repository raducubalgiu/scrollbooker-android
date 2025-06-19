package com.example.scrollbooker.screens.appointments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.shared.appointment.domain.model.Appointment
import com.example.scrollbooker.shared.appointment.domain.useCase.GetUserAppointmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val getUserAppointmentsUseCase: GetUserAppointmentsUseCase
): ViewModel() {

    private val _customerAppointments: Flow<PagingData<Appointment>> by lazy {
        getUserAppointmentsUseCase(asCustomer = false).cachedIn(viewModelScope)
    }

    private val _businessAppointments: Flow<PagingData<Appointment>> by lazy {
        getUserAppointmentsUseCase(asCustomer = true).cachedIn(viewModelScope)
    }

    val customerAppointments: Flow<PagingData<Appointment>> get() = _customerAppointments
    val businessAppointments: Flow<PagingData<Appointment>> get() = _businessAppointments

    init {
        Timber.tag("Init").e("-> Appointments - View Model Created")
    }
}