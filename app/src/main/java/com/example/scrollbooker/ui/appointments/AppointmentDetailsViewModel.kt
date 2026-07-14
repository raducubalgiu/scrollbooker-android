package com.example.scrollbooker.ui.appointments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentWrittenReview
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CancelAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetAppointmentByIdUseCase
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewCreateRequest
import com.example.scrollbooker.entity.booking.review.domain.useCase.CreateWrittenReviewUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.DeleteWrittenReviewUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.UpdateWrittenReviewUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.appointments.sheets.RatingReviewUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppointmentDetailsViewModel @Inject constructor(
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val createWrittenReviewUseCase: CreateWrittenReviewUseCase,
    private val updateWrittenReviewUseCase: UpdateWrittenReviewUseCase,
    private val deleteWrittenReviewUseCase: DeleteWrittenReviewUseCase,
    private val authDataStore: AuthDataStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val appointmentId: Int = checkNotNull(savedStateHandle["appointmentId"])

    private val _appointmentState = MutableStateFlow<FeatureState<Appointment>>(FeatureState.Loading)
    val appointmentState: StateFlow<FeatureState<Appointment>> = _appointmentState.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _createReviewState = MutableStateFlow<FeatureState<Unit>?>(null)
    val createReviewState: StateFlow<FeatureState<Unit>?> = _createReviewState.asStateFlow()

    private val _deleteReviewState = MutableStateFlow<FeatureState<Unit>?>(null)
    val deleteReviewState: StateFlow<FeatureState<Unit>?> = _deleteReviewState.asStateFlow()

    init {
        fetchAppointmentDetails()
    }

    private fun fetchAppointmentDetails() {
        viewModelScope.launch {
            _appointmentState.value = FeatureState.Loading
            try {
                val result = withVisibleLoading {
                    getAppointmentByIdUseCase(appointmentId)
                }
                _appointmentState.value = FeatureState.Success(result)
            } catch (e: Exception) {
                _appointmentState.value = FeatureState.Error(e)
            }
        }
    }

    suspend fun cancelAppointment(
        appointmentId: Int,
        canceledReason: String
    ): Result<Unit> {
        _isSaving.value = true

        val userId = authDataStore.getUserId().firstOrNull()
            ?: return Result.failure(Exception("User ID not found"))

        val result = withVisibleLoading {
            cancelAppointmentUseCase(appointmentId, canceledReason, userId)
        }

        result
            .onFailure { e ->
                Timber.tag("Appointments").e("ERROR: on Cancelling Appointment $e")
            }
            .onSuccess {
//                val currentState = _appointment.value
//                if (currentState is FeatureState.Success) {
//                    val currentAppointment = currentState.data
//
//                    if (currentAppointment.id == appointmentId) {
//                        val updatedAppointment = currentAppointment.copy(
//                            status = AppointmentStatusEnum.CANCELED,
//                            message = canceledReason
//                        )
//                        _appointment.value = FeatureState.Success(updatedAppointment)
//                    }
//                }
            }

        _isSaving.value = false
        return result
    }

    fun createReview(reviewUpdate: RatingReviewUpdate) {
        viewModelScope.launch {
            val currentState = _appointmentState.value
            if (currentState !is FeatureState.Success) {
                return@launch
            }
            val appointment = currentState.data

            _isSaving.value = true
            _createReviewState.value = FeatureState.Loading

            val userId = appointment.user.id ?: return@launch
            val firstProduct = appointment.products.firstOrNull() ?: return@launch
            val firstProductId = firstProduct.id ?: return@launch

            val request = ReviewCreateRequest(
                review = reviewUpdate.review,
                rating = reviewUpdate.rating,
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
                    _createReviewState.value = FeatureState.Error(e)
                    Timber.tag("Reviews").e("ERROR: on Creating Review $e")
                }
                .onSuccess { new ->
                    val latestState = _appointmentState.value
                    if (latestState is FeatureState.Success) {
                        val currentAppointment = latestState.data

                        if (currentAppointment.id == appointment.id) {
                            val updatedAppointment = currentAppointment.copy(
                                writtenReview = AppointmentWrittenReview(
                                    id = new.id,
                                    review = reviewUpdate.review,
                                    rating = reviewUpdate.rating
                                ),
                                hasWrittenReview = true
                            )
                            _appointmentState.value = FeatureState.Success(updatedAppointment)
                        }
                    }

                    _isSaving.value = false
                    _createReviewState.value = FeatureState.Success(Unit)
                }
        }
    }

    fun editReview(reviewUpdate: RatingReviewUpdate) {
        viewModelScope.launch {
            val currentState = _appointmentState.value
            if (currentState !is FeatureState.Success) {
                return@launch
            }
            val appointment = currentState.data
            val reviewId = appointment.writtenReview?.id ?: return@launch

            _isSaving.value = true
            _createReviewState.value = FeatureState.Loading

            val result = withVisibleLoading {
                updateWrittenReviewUseCase(
                    reviewId = reviewId,
                    review = reviewUpdate.review,
                    rating = reviewUpdate.rating
                )
            }

            result
                .onFailure { e ->
                    _isSaving.value = false
                    _createReviewState.value = FeatureState.Error(e)
                    Timber.tag("Reviews").e("ERROR: on Updating Review $e")
                }
                .onSuccess { update ->
                    val latestState = _appointmentState.value
                    if (latestState is FeatureState.Success) {
                        val currentAppointment = latestState.data

                        if (currentAppointment.id == update.appointmentId) {
                            val updatedAppointment = currentAppointment.copy(
                                writtenReview = AppointmentWrittenReview(
                                    id = update.id,
                                    review = reviewUpdate.review,
                                    rating = reviewUpdate.rating
                                ),
                                hasWrittenReview = true
                            )
                            _appointmentState.value = FeatureState.Success(updatedAppointment)
                        }
                    }

                    _isSaving.value = false
                    _createReviewState.value = FeatureState.Success(Unit)
                }
        }
    }

    fun deleteReview(appointmentId: Int, reviewId: Int) {
        viewModelScope.launch {
            _deleteReviewState.value = FeatureState.Loading
            _isSaving.value = true

            val result = withVisibleLoading { deleteWrittenReviewUseCase(reviewId) }

            result
                .onFailure { e ->
                    _isSaving.value = false
                    _deleteReviewState.value = FeatureState.Error()
                    Timber.tag("Reviews").e("ERROR: on Deleting Review $e")
                }
                .onSuccess {
//                    val currentState = _appointment.value
//
//                    if (currentState is FeatureState.Success) {
//                        val currentAppointment = currentState.data
//
//                        if (currentAppointment.id == appointmentId) {
//                            val updatedAppointment = currentAppointment.copy(
//                                writtenReview = null,
//                                hasWrittenReview = false
//                            )
//
//                            _appointment.value = FeatureState.Success(updatedAppointment)
//                        }
//                    }
//
//                    _isSaving.value = false
//                    _deleteReviewState.value = FeatureState.Success(Unit)
                }
        }
    }

    fun consumeCreateReviewState() {
        _createReviewState.value = null
    }

    fun consumeDeleteReviewState() {
        _deleteReviewState.value = null
    }
}
