package com.example.scrollbooker.ui.appointments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.DeleteAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val getUserAppointmentsUseCase: GetUserAppointmentsUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private val authDataStore: AuthDataStore
): ViewModel() {
    private val _asCustomer = MutableStateFlow<Boolean?>(null)

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _appointmentsState = MutableStateFlow<Flow<PagingData<Appointment>>?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val appointments: Flow<PagingData<Appointment>> =
        _appointmentsState.filterNotNull().flatMapLatest { it }

    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment: StateFlow<Appointment?> = _selectedAppointment

    private val _permissionsState = MutableStateFlow<FeatureState<List<PermissionEnum>>>(
        FeatureState.Loading)
    val permissionsState: StateFlow<FeatureState<List<PermissionEnum>>> = _permissionsState.asStateFlow()

    init {
        loadAppointments(null)
        loadPermissions()
    }

    fun loadPermissions() {
        viewModelScope.launch {
            try {
                val results = withVisibleLoading {
                    val rawList = authDataStore.getUserPermissions().first()
                    PermissionEnum.fromKeys(rawList)
                }
                _permissionsState.value = FeatureState.Success(results)
            } catch (e: Exception) {
                Timber.tag("Permissions dataStore").e("ERROR: on loading permission from authDataStore $e")
                FeatureState.Error()
            }
        }
    }

    fun loadAppointments(asCustomer: Boolean?) {
        _asCustomer.value = asCustomer
        _appointmentsState.value = getUserAppointmentsUseCase(asCustomer)
            .cachedIn(viewModelScope)
    }

    fun setAppointment(appointment: Appointment) {
        _selectedAppointment.value = appointment
    }

    suspend fun cancelAppointment(appointmentId: Int, message: String): Result<Unit> {
        _isSaving.value = true

        val result = withVisibleLoading {
            deleteAppointmentUseCase(appointmentId, message)
        }

        result
            .onFailure { e ->
                Timber.tag("Appointments").e("ERROR: on Cancelling Appointment $e")
            }
            .onSuccess {
                val currentFlow = _appointmentsState.value ?: return@onSuccess

                val updatedFlow = currentFlow.map { pagingData ->
                    pagingData.map { appointment ->
                        if (appointment.id == appointmentId) {
                            appointment.copy(
                                status = AppointmentStatusEnum.CANCELED.key,
                                message = message
                            )
                        } else appointment
                    }
                }.cachedIn(viewModelScope)

                _appointmentsState.value = updatedFlow

                val currentSelected = _selectedAppointment.value ?: return@onSuccess

                if(currentSelected.id == appointmentId) {
                    _selectedAppointment.value = currentSelected.copy(
                        status = AppointmentStatusEnum.CANCELED.key,
                        message = message
                    )
                }
            }

        _isSaving.value = false
        return result
    }
}