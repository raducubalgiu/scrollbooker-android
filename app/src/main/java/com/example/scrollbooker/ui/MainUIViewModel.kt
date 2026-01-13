package com.example.scrollbooker.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase,
): ViewModel() {
    private val _appointments = MutableStateFlow<Int>(0)
    val appointments: StateFlow<Int> = _appointments.asStateFlow()

    private val _notifications = MutableStateFlow<Int>(0)
    val notifications: StateFlow<Int> = _notifications.asStateFlow()

    private val _permissions = MutableStateFlow<Set<String>>(emptySet())
    val permissions: StateFlow<Set<String>> = _permissions.asStateFlow()

    private val _hasEmployees = MutableStateFlow<Boolean>(false)
    val hasEmployees: StateFlow<Boolean> = _hasEmployees

    init {
        viewModelScope.launch { loadAppointmentsNumber() }
        viewModelScope.launch { loadNotificationsNumber() }

        authDataStore.getUserPermissions()
            .map { it.toSet() }
            .distinctUntilChanged()
            .onEach { _permissions.value = it }
            .launchIn(viewModelScope)

        authDataStore.getHasEmployees()
            .distinctUntilChanged()
            .onEach { _hasEmployees.value = it }
            .launchIn(viewModelScope)
    }

    fun incAppointmentsNumber() {
        _appointments.value++
    }

    fun decAppointmentsNumber() {
        if(_appointments.value > 0) {
            _appointments.value--
        }
    }

    fun setAppointments(n: Int) {
        _appointments.value = n.coerceAtLeast(0)
    }

    fun setNotifications(n: Int) {
        _notifications.value = n.coerceAtLeast(0)
    }

    private suspend fun loadAppointmentsNumber() {
        _appointments.value = getUserAppointmentsNumberUseCase()
    }

    private suspend fun loadNotificationsNumber() {
        // To be implemented here with the notifications number
        _notifications.value = getUserAppointmentsNumberUseCase()
    }
}