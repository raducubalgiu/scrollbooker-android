package com.example.scrollbooker.ui
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import com.example.scrollbooker.entity.user.notification.domain.useCase.GetUserNotificationsNumberUseCase
import com.example.scrollbooker.navigation.bottomBar.MainTab
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
    private val getUserNotificationsNumberUseCase: GetUserNotificationsNumberUseCase,
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val authDataStore: AuthDataStore,
): ViewModel() {
    private companion object { const val KEY_TAB = "current_tab" }

    private val _currentTab = MutableStateFlow(
        MainTab.fromRoute(savedStateHandle.get<String>(KEY_TAB))
    )
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    fun setTab(tab: MainTab) {
        if (_currentTab.value == tab) return
        _currentTab.value = tab
        savedStateHandle[KEY_TAB] = tab.route
    }

    private val _appointments = MutableStateFlow<Int>(0)
    val appointments: StateFlow<Int> = _appointments.asStateFlow()

    private val _notifications = MutableStateFlow<Int>(0)
    val notifications: StateFlow<Int> = _notifications.asStateFlow()

    private val _permissions = MutableStateFlow<Set<String>>(emptySet())
    val permissions: StateFlow<Set<String>> = _permissions.asStateFlow()

    init {
        loadAppointmentsNumber()
        loadNotificationsNumber()

        authDataStore.getUserPermissions()
            .map { it.toSet() }
            .distinctUntilChanged()
            .onEach { _permissions.value = it }
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

    private fun loadAppointmentsNumber() {
        viewModelScope.launch {
            _appointments.value = getUserAppointmentsNumberUseCase()
        }
    }

    private fun loadNotificationsNumber() {
        viewModelScope.launch {
            _notifications.value = getUserNotificationsNumberUseCase()
        }
    }
}