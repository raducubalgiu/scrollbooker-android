package com.example.scrollbooker.ui.appointments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.DeleteAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsUseCase
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewCreateRequest
import com.example.scrollbooker.entity.booking.review.domain.useCase.CreateWrittenReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val getUserAppointmentsUseCase: GetUserAppointmentsUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private val createWrittenReviewUseCase: CreateWrittenReviewUseCase
): ViewModel() {
    private val _asCustomer = MutableStateFlow<Boolean?>(null)

    private val _reload = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    @OptIn(ExperimentalCoroutinesApi::class)
    val appointments: Flow<PagingData<Appointment>> =
        combine(_asCustomer, _reload.onStart { emit(Unit) }) { asCustomer, _ -> asCustomer }
            .flatMapLatest { asCustomer ->
                getUserAppointmentsUseCase(asCustomer)
            }
            .cachedIn(viewModelScope)

    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment: StateFlow<Appointment?> = _selectedAppointment

    init {
        loadAppointments(null)
    }

    fun loadAppointments(asCustomer: Boolean?) {
        _asCustomer.value = asCustomer
        _reload.tryEmit(Unit)
    }

    fun refreshAppointments() {
        _reload.tryEmit(Unit)
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
                _reload.tryEmit(Unit)

                _selectedAppointment.value = _selectedAppointment.value?.let { current ->
                    if(current.id == appointmentId) {
                        current.copy(
                            status = AppointmentStatusEnum.CANCELED,
                            message = message
                        )
                    } else current
                }
            }

        _isSaving.value = false
        return result
    }

    fun createReview(
        appointment: Appointment?,
        review: String?,
        rating: Int
    ) {
        viewModelScope.launch {
            _isSaving.value = true

            val appointment = appointment ?: return@launch
            val userId = appointment.user.id ?: return@launch
            val firstProduct = appointment.products.firstOrNull() ?: return@launch
            val firstProductId = firstProduct.id ?: return@launch

            val request = ReviewCreateRequest(
                review = review,
                rating = rating,
                userId = userId,
                productId = firstProductId,
                parentId = null
            )

            val result = withVisibleLoading {
                createWrittenReviewUseCase(appointment.id, request)
            }

            result
                .onFailure { e ->
                    _isSaving.value = false
                    Timber.tag("Appointments").e("ERROR: on Cancelling Appointment $e")
                }
                .onSuccess {
                    _isSaving.value = false
                }
        }
    }
}