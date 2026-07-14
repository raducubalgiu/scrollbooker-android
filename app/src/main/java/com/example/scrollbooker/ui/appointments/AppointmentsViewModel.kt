package com.example.scrollbooker.ui.appointments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val getUserAppointmentsUseCase: GetUserAppointmentsUseCase,
): ViewModel() {
    private val _asCustomer = MutableStateFlow<Boolean?>(null)
    private val _reload = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val appointments: Flow<PagingData<Appointment>> =
        combine(_asCustomer, _reload.onStart { emit(Unit) }) { asCustomer, _ -> asCustomer }
            .flatMapLatest { asCustomer -> getUserAppointmentsUseCase(asCustomer) }
            .cachedIn(viewModelScope)

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        _reload.tryEmit(Unit)
    }
}