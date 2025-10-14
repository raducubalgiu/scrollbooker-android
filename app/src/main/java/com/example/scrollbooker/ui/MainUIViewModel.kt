package com.example.scrollbooker.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase,
): ViewModel() {
    // Appointments State
    private val _appointmentsState = MutableStateFlow<Int>(0)
    val appointmentsState: StateFlow<Int> = _appointmentsState.asStateFlow()

    // Notifications State
    private val _notificationsState = MutableStateFlow<Int>(0)
    val notificationsState: StateFlow<Int> = _notificationsState.asStateFlow()

    fun increaseAppointmentsNumber() {
        _appointmentsState.value = _appointmentsState.value + 1
    }

    fun decreaseAppointmentsNumber() {
        if(_appointmentsState.value > 0) {
            _appointmentsState.value = _appointmentsState.value - 1
        }
    }

    private fun loadAppointmentsNumber() {
        viewModelScope.launch {
            _appointmentsState.value = getUserAppointmentsNumberUseCase()
        }
    }

    init {
        viewModelScope.launch {
            launch { loadAppointmentsNumber() }
        }
    }
}